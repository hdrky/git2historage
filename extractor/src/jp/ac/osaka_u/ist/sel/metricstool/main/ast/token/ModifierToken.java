package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * �C���q��\���g�[�N��
 * 
 * @author kou-tngt
 *
 */
public class ModifierToken extends AstTokenAdapter {
    
    
    /**
     * �w�肳�ꂽ������̏C���q��\���g�[�N�����쐬����
     * @param text �C���q�̕�����
     */
    public ModifierToken(final String text) {
        super(text);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isModifier()
     */
    @Override
    public boolean isModifier() {
        return true;
    }
}
