package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * {@link AstVisitor} ���ǂ̂悤��AST�̃m�[�h��K�₷�邩�𐧌䂷��C���^�t�F�[�X.
 * 
 * @author kou-tngt
 *
 * @param <T> �r�W�^�[���K�₷��AST�̊e�m�[�h�̌^
 */
public interface AstVisitStrategy<T> {

    /**
     * �r�W�^�[�����݂̃m�[�h�̎q�m�[�h��K�₷��K�v�����邩�ǂ�����Ԃ�.
     * 
     * @param node �r�W�^�[�����ݓ��B���Ă���m�[�h
     * @param token �r�W�^�[�����ݓ��B���Ă���m�[�h�̎�ނ�\���g�[�N��
     */
    public boolean needToVisitChildren(T node, AstToken token);

}
