package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * �A�N�Z�X�C���q��\���g�[�N���N���X.
 * 
 * @author kou-tngt
 *
 */
public class AccessModifierToken extends ModifierToken {

    /**
     * �����Ŏw�肳�ꂽ������Ɖ������������A�N�Z�X�C���q�g�[�N�����쐬����R���X�g���N�^
     * 
     * @param text �C���q�̕�����
     * @param publicVisibility ���̏C���q��t����ꂽ�v�f���p�u���b�N�ȉ����������ǂ���
     * @param nameSpaceVisibility ���̏C���q��t����ꂽ�v�f���������O��Ԃ���̉����������ǂ���
     * @param inheritanceVisibility ���̏C���q��t����ꂽ�v�f���p���֌W�ɂ���N���X����̉����������ǂ���
     */
    public AccessModifierToken(final String text, final boolean publicVisibility, final boolean nameSpaceVisibility,
            final boolean inheritanceVisibility) {
        super(text);
        this.publicVisibility = publicVisibility;
        this.nameSpaceVisibility = nameSpaceVisibility;
        this.inheritanceVisibility = inheritanceVisibility;
    }

    @Override
    public boolean isAccessModifier() {
        return true;
    }

    /**
     * ���̏C���q��t����ꂽ�v�f���p�u���b�N�ȉ����������ǂ�����Ԃ�
     * @return�@�p�u���b�N�ȉ��������ꍇ��true
     */
    public boolean isPublicVisibility() {
        return this.publicVisibility;
    }

    /**
     * ���̏C���q��t����ꂽ�v�f���������O��Ԃ���̉����������ǂ�����Ԃ�
     * @return �������O��Ԃ���̉��������ꍇ��true
     */
    public boolean isNameSpaceVisibility() {
        return this.nameSpaceVisibility;
    }

    /**
     * ���̏C���q��t����ꂽ�v�f���p���֌W�ɂ���N���X����̉����������ǂ�����Ԃ�
     * @return �p���֌W�ɂ���N���X����̉��������ꍇ��true
     */
    public boolean isInheritanceVisibility() {
        return this.inheritanceVisibility;
    }

    /**
     * �p�u���b�N�ȉ����������ǂ�����\��
     */
    private final boolean publicVisibility;

    /**
     * �������O��Ԃ���̉����������ǂ�����\��
     */
    private final boolean nameSpaceVisibility;

    /**
     * �p���֌W����̉����������ǂ�����\��
     */
    private final boolean inheritanceVisibility;
}
