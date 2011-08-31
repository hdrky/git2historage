package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



/**
 * �^T�ŕ\�������AST�̃m�[�h���C {@link AstToken}�ɖ|�󂷂�.
 * 
 * @author kou-tngt
 *
 * @param <T> �|�󂳂��AST�m�[�h�̌^
 */
public interface AstTokenTranslator<T> {

    /**
     * ����node���\��AST�m�[�h��{@link AstToken}�ɖ|�󂷂�.
     * 
     * @param node �|�󂷂�AST�m�[�h
     * @return �|�󌋉ʂ̃g�[�N��
     */
    public AstToken translate(T node);

}
