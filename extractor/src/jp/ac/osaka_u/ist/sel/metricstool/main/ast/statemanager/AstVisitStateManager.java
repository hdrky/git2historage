package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;

/**
 * ���ۍ\���؂̃r�W�^�[���ǂ̂悤�ȏ�Ԃɂ��邩���Ǘ����C��ԕω��C�x���g�𔭍s����.
 * ���̃C���^�t�F�[�X�����������N���X�Q����̃C�x���g���󂯎��C������g�ݍ��킹�邱�Ƃł��ƂŁC
 * �r�W�^�[������AST�̂ǂ̕����ɓ��B���Ă��邩�𔻒f���邱�Ƃ��ł���.
 * 
 * @author kou-tngt
 *
 */
public interface AstVisitStateManager extends StateManager<AstVisitEvent>, AstVisitListener{

}
