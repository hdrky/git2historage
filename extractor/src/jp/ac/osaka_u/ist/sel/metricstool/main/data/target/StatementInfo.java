package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * @author higo
 *
 */
public interface StatementInfo extends ExecutableElementInfo {

    /**
     * ���̃e�L�X�g�\��(String�^)��Ԃ�
     * 
     * @return�@���̃e�L�X�g�\��(String�^)��Ԃ�
     */
    String getText();

    /**
     * ���𒼐ڏ��L�����Ԃ�Ԃ�
     * @return ���𒼐ڏ��L������
     */
    LocalSpaceInfo getOwnerSpace();

    /**
     * �������L���郁�\�b�h�܂��̓R���X�g���N�^��Ԃ�
     * 
     * @return �������L���郁�\�b�h�܂��̓R���X�g���N�^
     */
    CallableUnitInfo getOwnerMethod();
}
