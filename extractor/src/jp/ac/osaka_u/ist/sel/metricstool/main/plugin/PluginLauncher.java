package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.util.Collection;


/**
 * �v���O�C�����s���̃C���^�t�F�[�X
 * @author kou-tngt
 *
 */
public interface PluginLauncher {
    /**
     * �v���O�C�������s����.
     * @param plugin ���s����v���O�C��
     */
    public void launch(AbstractPlugin plugin);

    /**
     * �v���O�C�����܂Ƃ߂Ď��s����
     * @param plugins ���s����v���O�C���̃R���N�V����
     */
    public void launchAll(Collection<AbstractPlugin> plugins);

    /**
     * �v���O�C���̎��s���L�����Z������
     * @param plugin �L�����Z������v���O�C��
     */
    public boolean cancel(AbstractPlugin plugin);

    /**
     * �v���O�C���̎��s���܂Ƃ߂ăL�����Z������
     * @param plugins �L�����Z������v���O�C��
     */
    public void cancelAll(Collection<AbstractPlugin> plugins);

    /**
     * ���s���C���s�҂��̃v���O�C���̎��s��S�ăL�����Z������
     */
    public void cancelAll();

    /**
     * �v���O�C���������s�ő吔��ݒ肷�郁�\�b�h
     * @param num �v���O�C���������s�ő吔
     */
    public void setMaximumLaunchingNum(int num);

    /**
     * �����`���[���~����.
     * ���s�҂��̃^�X�N�͍폜���C���s���̃^�X�N�͏I���܂ő҂�.
     */
    public void stopLaunching();

    /**
     * �����`���[�𒼂��ɒ�~����.
     * ���s�҂��̃^�X�N�͍폜���C���s���̃^�X�N�͑S�ăL�����Z�������.
     */
    public void stopLaunchingNow();

    /**
     * ���s�҂��̃^�X�N�̐���Ԃ�.
     */
    public int getLaunchWaitingTaskNum();

    /**
     * �������s�ő吔��Ԃ����\�b�h
     * @return �������s�ő吔
     */
    public int getMaximumLaunchingNum();

    /**
     * ���ݎ��s����Ă���v���O�C���̐���Ԃ�
     * @return ���ݎ��s����Ă���v���O�C���̐�
     */
    public int getCurrentLaunchingNum();
}
