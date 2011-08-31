package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * ���ʎq��\���g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class IdentifierToken extends AstTokenAdapter {

    /**
     * �w�肳�ꂽ������̎��ʎq��\���C���X�^���X���쐬����.
     * @param text ���ʎq�̖��O
     */
    public IdentifierToken(final String text) {
        super(text);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isIdentifier()
     */
    @Override
    public boolean isIdentifier() {
        return true;
    }
}
