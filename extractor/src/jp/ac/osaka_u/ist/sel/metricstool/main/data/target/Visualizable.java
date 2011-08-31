package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �N���X��C�t�B�[���h�C���\�b�h�̉������`����C���^�[�t�F�[�X�D �ȉ��̉������`����D
 * 
 * <ul>
 * <li>�q�N���X����Q�Ɖ\</li>
 * <li>�������O��ԓ�����Q�Ɖ\</li>
 * <li>�ǂ�����ł��Q�Ɖ\</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public interface Visualizable {

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    boolean isNamespaceVisible();

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    boolean isInheritanceVisible();

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    boolean isPublicVisible();
}
