package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;




/**
 * {@link AstVisitor} �ւ̑���Ɛݒ���Ǘ�����C���^�t�F�[�X.
 * 
 * @author kou-tngt
 *
 * @param <T>�@�Ǘ�����r�W�^�[���K�₷��AST�̃m�[�h�̌^
 */
public interface AstVisitorManager<T> {

    /**
     * ����node���\���m�[�h����Ǘ�����r�W�^�[�̖K����J�n����.
     * 
     * @param node�@�r�W�^�[�̖K����J�n����m�[�h
     */
    public void visitStart(T node) throws ASTParseException;
    
    public void reset();

}