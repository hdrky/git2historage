package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;


/**
 * ������C�}�W�b�N�i���o�[�Ȃǂ̒萔�l�g�[�N����\���N���X
 * 
 * @author kou-tngt
 *
 */
public class ConstantToken extends AstTokenAdapter {

    /**
     * ����text�Ŏw�肳�ꂽ������ŋL�q����C����type�Ŏw�肳�ꂽ�^�̒萔��\���g�[�N�����쐬����
     * 
     * @param text�@�萔�Ƃ��ċL�q���ꂽ������
     * @param type�@�萔�̌^
     * @throws type��null�̏ꍇ
     */
    public ConstantToken(final String text, final TypeInfo type) {
        super(text);

        if (null == type) {
            throw new NullPointerException("type is null.");
        }
        this.type = type;
    }

    /**
     * ���̃g�[�N�����\���萔�̌^��Ԃ�.
     * @return ���̃g�[�N�����\���萔�̌^
     */
    public TypeInfo getType() {
        return this.type;
    }

    /**
     * �萔��\���g�[�N�����ǂ�����Ԃ��D
     * @return true
     */
    @Override
    public boolean isConstant() {
        return true;
    }

    /**
     * ���̃g�[�N�����\���萔�̌^
     */
    private final TypeInfo type;

}
