package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �\�t�g�E�F�A�̒P�ʂ��C���X�^���X�Ȃ̂��X�^�e�B�b�N�Ȃ̂����`����C���^�[�t�F�[�X
 * 
 * @author higo
 */
public interface StaticOrInstance {

    /**
     * �C���X�^���X�����o�[���ǂ�����Ԃ�
     * 
     * @return �C���X�^���X�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    boolean isInstanceMember();

    /**
     * �X�^�e�B�b�N�����o�[���ǂ�����Ԃ�
     * 
     * @return �X�^�e�B�b�N�����o�[�̏ꍇ true�C�����łȂ��ꍇ false
     */
    boolean isStaticMember();
}
