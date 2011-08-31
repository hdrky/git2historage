package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import java.util.EventListener;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;


/**
 * ���ۍ\���؂̃r�W�^�[���� {@link AstVisitEvent} �̒ʒm���󂯎��C���^�t�F�[�X.
 * 
 * �C�ӂ̃m�[�ha�ɂ���,{@link #visited(AstVisitEvent)},{@link #entered(AstVisitEvent)},{@link #exited(AstVisitEvent)}
 * �̏��Ԃɒʒm�����.
 * �؍\���̗t�ł���m�[�h�ɂ��Ă��C {@link #entered(AstVisitEvent)}��{@link #exited(AstVisitEvent)}���\�b�h���Ă΂��.
 * 
 * @author kou-tngt
 *
 */
public interface AstVisitListener extends EventListener {
    /**
     * �r�W�^�[�����钸�_�ɓ��B�������ɃC�x���g���󂯎��.
     * @param e ���B�C�x���g
     */
    public void visited(AstVisitEvent e);

    /**
     * �r�W�^�[�����钸�_�̒��ɓ��鎞�ɃC�x���g���󂯎��.
     * @param e
     */
    public void entered(AstVisitEvent e);

    /**
     * �r�W�^�[�����钸�_�̒�����o�����ɃC�x���g���󂯎��
     * @param e
     */
    public void exited(AstVisitEvent e) throws ASTParseException;
}
