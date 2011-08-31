package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ClosableLinkedBlockingQueue;


/**
 * �v���O�C�������`���[�̃��b�p�[�N���X.
 * ���ʌ��������X���b�h�ȊO�̓C���X�^���X���ł��Ȃ�.
 * ���̃X���b�h�̃C���X�^���X�͕K�����ʌ���������.
 * ���̃��b�p�[�N���X��ʂ����ƂŁC�{�����ʌ������K�v�ȃv���O�C�����s���\�b�h�ւ̃A�N�Z�X����ʌ��������Ŏ��s���邱�Ƃ��ł���.
 * �䂦�ɁC���̃N���X���C���X�^���X���������ʌ��������X���b�h�́C���̃C���X�^���X���v���O�C���Ɏ擾����Ȃ��悤�ɒ��ӂ��Ȃ���΂Ȃ�Ȃ�.
 * <p>
 * �S�Ẵv���O�C���̎��s���I�������ɁC�K�� {@link #stopLaunching()}�܂���
 * {@link #stopLaunchingNow()}���Ă΂Ȃ���΂Ȃ�Ȃ�.
 * @author kou-tngt
 *
 */
public class PluginLauncherThread extends Thread implements PluginLauncher {
    /**
     * �B��̃R���X�g���N�^
     * ���ʌ����ȊO�̃X���b�h����Ăяo���ꂽ�ꍇ�� {@link AccessControlException}���X���[����
     * ���̃R���X�g���N�^�ɂ���Đ��������X���b�h�C���X�^���X�͓��ʌ�����t�^�����
     * @throws AccessControlException �Ăяo�����X���b�h�����ʌ����������Ă��Ȃ������ꍇ
     */
    public PluginLauncherThread(final PluginLauncher launcher) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        MetricsToolSecurityManager.getInstance().addPrivilegeThread(this);
        this.launcher = launcher;
    }

    /**
     * ���s�̃L�����Z���v�������郁�\�b�h
     * @param plugin �L�����Z���������v���O�C��
     * @throws NullPointerException plugin��null�̏ꍇ
     */
    public synchronized boolean cancel(final AbstractPlugin plugin) {
        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }
        this.cancelQueue.offer(plugin);
        this.notify();
        return true;

    }

    /**
     * �L�����Z���v���܂Ƃ߂ēo�^���郁�\�b�h
     * @param plugins �L�����Z������v���O�C���Q���܂ރR���N�V����
     * @throws NullPointerException plugins��null�̏ꍇ
     */
    public void cancelAll(final Collection<AbstractPlugin> plugins) {
        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        if (!this.stopFlag) {
            for (final AbstractPlugin plugin : plugins) {
                this.cancel(plugin);
            }
        }
    }

    /**
     * ���s���C���s�҂��̃v���O�C���̎��s��S�ăL�����Z������
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher#cancelAll()
     */
    public void cancelAll() {
        this.requestCancelAll = true;
    }
    
    /**
     * ���s�҂��̃^�X�N�̐���Ԃ�.
     * @return ���s�҂��̃^�X�N�̐�
     */
    public int getLaunchWaitingTaskNum(){
        return this.launcher.getLaunchWaitingTaskNum() + this.launchQueue.size();
    }

    /**
     * ���ݎ��s���̃v���O�C���̐���Ԃ����\�b�h.
     * @return ���s���̃v���O�C���̐�.
     */
    public int getCurrentLaunchingNum() {
        return this.launcher.getCurrentLaunchingNum();
    }

    /**
     * �v���O�C���̓������s�ő吔��Ԃ����\�b�h
     * @return �������s�ő吔
     */
    public int getMaximumLaunchingNum() {
        return this.launcher.getMaximumLaunchingNum();
    }

    /**
     * ���s�v����o�^���郁�\�b�h
     * @param plugin ���s����v���O�C��
     * @throws NullPointerException plugin��null�̏ꍇ
     */
    public synchronized void launch(final AbstractPlugin plugin) {
        if (null == plugin) {
            throw new NullPointerException("plugin is null.");
        }

        this.launchQueue.offer(plugin);
        this.notify();
    }

    /**
     * ���s�v�����܂Ƃ߂ēo�^���郁�\�b�h
     * @param plugins ���s����v���O�C���Q���܂ރR���N�V����
     * @throws NullPointerException plugins��null�̏ꍇ
     */
    public void launchAll(final Collection<AbstractPlugin> plugins) {
        if (null == plugins) {
            throw new NullPointerException("plugins is null.");
        }

        if (!this.stopFlag) {
            for (final AbstractPlugin plugin : plugins) {
                this.launch(plugin);
            }
        }
    }

    /**
     * ���̃X���b�h�̎��s���\�b�h
     * ��~�M��������܂ŁC�o�^���ꂽ���s�v����L�����Z���v�����L���[������o���ď�������.
     * 
     */
    @Override
    public void run() {
        while (!this.stopNowFlag
                && (!this.stopFlag || !this.launchQueue.isEmpty() || !this.cancelQueue.isEmpty())) {
            //������~�M�������Ă��Ȃ��@���@�i��~�M�������ĂȂ� or �d�����c���Ă�j
            synchronized (this) {
                //���s���N�G�X�g�����Ă���v���O�C�������s����
                while (!this.launchQueue.isEmpty()) {
                    this.launcher.launch(this.launchQueue.poll());
                }

                //�L�����Z�����N�G�X�g�����Ă���v���O�C�����L�����Z������
                //launcher�ŃL�����Z���Ɏ��s������C�������̃L���[�ɂ���\��������̂ŁC�T���č폜����
                while (!this.cancelQueue.isEmpty()) {
                    final AbstractPlugin plugin = this.cancelQueue.poll();
                    if (!this.launcher.cancel(plugin)) {
                        for (final Iterator<AbstractPlugin> it = this.launchQueue.iterator(); it.hasNext();) {
                            if (it.next() == plugin) {
                                it.remove();
                                break;
                            }
                        }
                    }
                }
                //�S�L�����Z���̃��N�G�X�g�������̂ŁC�S�L�����Z������
                if (this.requestCancelAll) {
                    this.requestCancelAll = false;
                    this.launcher.cancelAll();
                    this.launchQueue.clear();
                }
                
                //�ő哯�����s���̕ύX�v���������̂ŕύX����
                if (this.maximumLaunchingNumRequest > 0){
                    this.launcher.setMaximumLaunchingNum(this.maximumLaunchingNumRequest);
                    this.maximumLaunchingNumRequest = 0;
                }

                if (!this.stopNowFlag && !this.stopFlag) {
                    //�܂���~�M�������ĂȂ��̂ŁC�N�����N�����ɗ���܂ŐQ��
                    try {
                        this.wait();
                    } catch (final InterruptedException e) {
                        //���荞�܂�Ă��C�ɂ��Ȃ�
                    }
                }
            }
        }
        //��~�M������������`���[�ɑ���
        if (this.stopNowFlag) {
            this.launcher.stopLaunchingNow();
        } else if (this.stopFlag) {
            this.launcher.stopLaunching();
        }
    }

    /**
     * �v���O�C���̓������s�ő吔���Z�b�g���郁�\�b�h
     * @param size �ő吔���w�肷��C1�ȏ�̐���
     * @throws IllegalArgumentException size��0�ȉ��������ꍇ
     */
    public void setMaximumLaunchingNum(final int size) {
        if (1 > size) {
            throw new IllegalArgumentException("size must be a natural number.");
        }

        this.maximumLaunchingNumRequest = size;
    }

    /**
     * ���̃X���b�h�ɒ�~�M���𑗂郁�\�b�h.
     * ���s�҂��̃^�X�N�͍폜���C���s���̃v���O�C���͏I������܂ő҂�.
     */
    public synchronized void stopLaunching() {
        this.stopFlag = true;
        this.launchQueue.close();
        this.launchQueue.clear();
        this.notify();
    }

    /**
     * �����`���[�𒼂��ɒ�~����. 
     * ���s�҂��̃^�X�N�͍폜���C���s���̃^�X�N�͑S�ăL�����Z�������. 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher#stopLaunchingNow()
     */
    public void stopLaunchingNow() {
        this.stopNowFlag = true;
        this.cancelQueue.clear();
        this.stopLaunching();
    }

    /**
     * ���s�v���L���[
     */
    private final ClosableLinkedBlockingQueue<AbstractPlugin> launchQueue = new ClosableLinkedBlockingQueue<AbstractPlugin>();

    /**
     * �L�����Z���v���L���[
     */
    private final BlockingQueue<AbstractPlugin> cancelQueue = new LinkedBlockingQueue<AbstractPlugin>();

    /**
     * �����ŗ��p���郉���`���[
     */
    private final PluginLauncher launcher;

    /**
     * ��~�M��
     */
    private boolean stopFlag = false;

    /**
     * ������~�M��
     */
    private boolean stopNowFlag = false;

    /**
     * �^�X�N�̑S�L�����Z���v��
     */
    private boolean requestCancelAll = false;
    
    /**
     * �^�X�N�ő���s���̕ύX�v��������ϐ�
     */
    private int maximumLaunchingNumRequest = 0;

}
