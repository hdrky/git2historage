package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;


/**
 * �r�W�^�[�̓���𐧌䂷�邽�߂ɗ\�ߗp�ӂ��ꂽ�萔�g�[�N�������N���X.
 * 
 * @author kou-tngt
 *
 */
public class VisitControlToken extends AstTokenAdapter {

    /**
     * ���̃g�[�N���ŕ\�����m�[�h�̒��ɂ̓r�W�^�[�͓���Ȃ�.
     */
    public static final VisitControlToken SKIP = new VisitControlToken("SKIP");

    /**
     * ���̃g�[�N���ŕ\�����m�[�h���̂ɂ͓��ʂȏ����͂��Ȃ����C�r�W�^�[�͂��̃m�[�h�̎q�m�[�h��K�₵�ɍs��.
     */
    public static final VisitControlToken ENTER = new VisitControlToken("ENTER");

    /**
     * ����`�̃m�[�h��\������g�[�N��.
     * ���̃g�[�N���ŕ\�����m�[�h�ɑ΂��Ăǂ̂悤�ȏ������s������ {@link AstVisitStrategy}�̎����ɔC�����.
     */
    public static final VisitControlToken UNKNOWN = new VisitControlToken("UNKNOWN");

    /**
     * �C���X�^���X�̎�ނ�\�����镶�����^����R���X�g���N�^.
     * @param text
     */
    private VisitControlToken(final String text) {
        super(text);
    }
}
