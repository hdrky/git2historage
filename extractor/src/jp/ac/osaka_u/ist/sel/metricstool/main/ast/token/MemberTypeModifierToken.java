package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * �N���X�����o�̎�ނ��w�肷��C���q��\���g�[�N��
 * 
 * @author kou-tngt
 *
 */
public class MemberTypeModifierToken extends ModifierToken{

    /**
     * static�����o��\���萔�C���X�^���X
     */
    public static final MemberTypeModifierToken STATIC = new MemberTypeModifierToken("static");
    
    /**
     * @param text
     */
    public MemberTypeModifierToken(String text) {
        super(text);
    }
    
    /**
     * static�����o���ǂ�����Ԃ�
     * @return static�����o�ł����true,�����łȂ��Ȃ�false
     */
    public boolean isStaticMember(){
        return this.equals(STATIC);
    }
}
