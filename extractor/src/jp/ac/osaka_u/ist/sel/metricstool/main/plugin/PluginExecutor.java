package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.security.AccessControlException;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * ���ۂɃv���O�C�������s����N���X.
 * �C���X�^���X���ɂ͓��ʌ������K�v.
 * @author kou-tngt
 *
 */
public class PluginExecutor implements Runnable {
    /**
     * �����̃v���O�C�������s����C���X�^���X�𐶐�����.
     * ���ʌ��������N���X����̂݌Ăяo�����Ƃ��ł���.
     * @param plugin ���s����v���O�C��
     * @throws AccessControlException ���ʌ����������Ȃ��X���b�h����Ă΂ꂽ�ꍇ
     */
    public PluginExecutor(final AbstractPlugin plugin) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.plugin = plugin;
    }

    /**
     * ���s���I���������ɁC���s�X���b�h����Ăяo����郊�X�i��o�^���郁�\�b�h
     * @param listener �o�^���郊�X�i
     * @throws NullPointerException listener��null�̏ꍇ
     */
    public void addExecutionEndListener(final ExecutionEndListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null.");
        }

        this.listeners.add(listener);
    }

    /**
     * ���s���\�b�h
     */
    public void execute() {
        this.plugin.executionWrapper();
        this.fireExecutionEnd();
    }

    /**
     * �v���O�C�����擾����
     * @return �v���O�C�����擾����
     */
    public AbstractPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * �ʃX���b�h�Ƃ��ċN�������ꍇ�C�̃G���g�����\�b�h.
     * {@link #execute()} ���Ăяo���݂̂ł���.
     * @see java.lang.Runnable#run()
     */
    public void run() {
        this.execute();
    }

    /**
     * ���s���I���������ɁC���s�X���b�h����Ăяo����郊�X�i���폜���郁�\�b�h
     * @param listener �폜���������X�i
     */
    public void removeExectionEndListener(final ExecutionEndListener listener) {
        if (null != listener) {
            this.listeners.remove(listener);
        }
    }

    /**
     * ���s�I�������X�i�ɒʒm����.
     */
    private void fireExecutionEnd() {
        for (final ExecutionEndListener listener : this.listeners) {
            listener.executionEnd(this.plugin);
        }
    }

    /**
     * ���s����v���O�C��
     */
    private final AbstractPlugin plugin;

    /**
     * ���X�i��Set
     */
    private final Set<ExecutionEndListener> listeners = new WeakHashSet<ExecutionEndListener>();

}
