package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;




/**
 * �C�ӂ̍\����AST��K�₷��r�W�^�[�̃C���^�t�F�[�X.
 * <p>
 * ���̃C���^�t�F�[�X����������N���X�́CAST�m�[�h�����ԂɖK�₵�C
 * �e�m�[�h�ɓ��B�������C���̃m�[�h�̓����ɓ��鎞�C���̃m�[�h�̓�������o�鎞�ɁC
 * �o�^���ꂽ {@link AstVisitListener} �ɑ΂��ēK�؂ȃC�x���g�𔭍s����.
 * 
 * @author kou-tngt
 *
 * @param <T>�@�K�₷��AST�m�[�h�̌^
 */
public interface AstVisitor<T> {

    /**
     * ���̃r�W�^�[�����s����e {@link AstVisitEvent} �̒ʒm���󂯂郊�X�i��o�^����.
     * 
     * @param listener �o�^���郊�X�i
     * @throws NullPointerException listener��null�̏ꍇ
     */
    public void addVisitListener(AstVisitListener listener);

    /**
     * ���̃r�W�^�[�����s����e {@link AstVisitEvent} �̒ʒm���󂯂郊�X�i���폜����.
     * 
     * @param listener�@�폜���郊�X�i
     * @throws NullPointerException listener��null�̏ꍇ
     */
    public void removeVisitListener(AstVisitListener listener);

    /**
     * ���̃r�W�^�[�̏�Ԃ�������Ԃɖ߂�.
     * �C�x���g���X�i�͍폜����Ȃ�.
     */
    public void reset();

    /**
     * �r�W�^�[�̖K����J�n����
     * @param startNode�@�ŏ��ɖK�₷��AST�m�[�h
     */
    public void startVisiting(T startNode) throws ASTParseException;
}
