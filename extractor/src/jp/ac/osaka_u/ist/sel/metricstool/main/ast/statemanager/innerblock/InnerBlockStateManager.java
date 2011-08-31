package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.DeclaredBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �����u���b�N�̉�͏�Ԃ��Ǘ�����N���X
 * 
 * @author t-miyake
 *
 */
public abstract class InnerBlockStateManager extends DeclaredBlockStateManager {

    public static enum INNER_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_BLOCK_DEF, EXIT_BLOCK_DEF,

        ENTER_CLAUSE, EXIT_CLAUSE,

        ENTER_BLOCK_SCOPE, EXIT_BLOCK_SCOPE
    }
    
    public static enum INNER_BLOCK_STATE implements DeclaredBlockState  {
        CONDITIONAL_CLAUSE
    }

    @Override
    protected boolean fireStateChangeEnterEvent(final AstVisitEvent event) {
        // ���ɃC�x���g�����s�ς݂̏ꍇ�C���������I��
        if (super.fireStateChangeEnterEvent(event)) {
            return true;
        }

        if (this.isConditionalClause(event.getToken()) && STATE.DECLARE == this.getState()){
            //��`���ɂ����Ԃŏ����߂�\���m�[�h������Ώ�ԑJ�ڂ��ăC�x���g�𔭍s
            this.setState(INNER_BLOCK_STATE.CONDITIONAL_CLAUSE);
            this.fireStateChangeEvent(INNER_BLOCK_STATE_CHANGE.ENTER_CLAUSE, event);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean fireStateChangeExitEvent(AstVisitEvent event) {
        // ���ɃC�x���g�����s�ς݂̏ꍇ�C���������I��
        if (super.fireStateChangeExitEvent(event)) {
            return true;
        }
        
        if (this.isConditionalClause(event.getToken()) && STATE.DECLARE == this.getState()) {
            //��`���ɂ����ԂŃu���b�N��\���m�[�h������΃C�x���g�𔭍s
            this.fireStateChangeEvent(INNER_BLOCK_STATE_CHANGE.EXIT_CLAUSE, event);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_SCOPE;
    }

    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_SCOPE;
    }

    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_DEF;
    }

    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_DEF;
    }

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return super.isStateChangeTriggerEvent(event) || this.isConditionalClause(event.getToken());
    }
    
    protected boolean isConditionalClause(AstToken token) {
        return token.isConditionalClause();
    }
}
