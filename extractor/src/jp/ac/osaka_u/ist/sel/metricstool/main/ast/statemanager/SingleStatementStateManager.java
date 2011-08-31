package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public abstract class SingleStatementStateManager extends
        StackedAstVisitStateManager<SingleStatementStateManager.SingleStatementState> {

    public interface SingleStatementState {
    }

    protected static enum STATE implements SingleStatementState {
        IN, OUT
    }

    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        if (this.isStateChangeTriggerEvent(event)) {
            //��ԕω��g���K�Ȃ�
            fireStateChangeEnterEvent(event);
        }
    }

    protected void fireStateChangeEnterEvent(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isHeaderToken(token)) {
            //��`�m�[�h�Ȃ��ԑJ�ڂ��ăC�x���g�𔭍s
            this.setState(STATE.IN);
            this.fireStateChangeEvent(this.getStatementEnterEventType(), event);
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {

        if (this.isStateChangeTriggerEvent(event)) {
            //��ԕω��g���K�Ȃ�

            //�X�^�b�N�̈�ԏ�̏�Ԃɖ߂�
            super.exited(event);

            fireStateChangeExitEvent(event);
        }
    }

    protected void fireStateChangeExitEvent(final AstVisitEvent event) {
        if (this.isHeaderToken(event.getToken())) {
            //��`�m�[�h�Ȃ�C�x���g�𔭍s
            this.fireStateChangeEvent(this.getStatementExitEventType(), event);
        }
    }
    
    protected boolean isInStatement() {
        return STATE.IN == this.getState();
    }

    public abstract StateChangeEventType getStatementEnterEventType();

    public abstract StateChangeEventType getStatementExitEventType();

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return this.isHeaderToken(event.getToken());
    }

    protected abstract boolean isHeaderToken(final AstToken token);

}
