package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

/**
 * �r�W�^�[���^�L�q���֏o���肷��ۂ̏�Ԃ��Ǘ�����X�e�[�g�}�l�[�W���D
 * 
 * @author kou-tngt
 *
 */
public class TypeDescriptionStateManager extends EnterExitStateManager{
    
    /**
     * @author kou-tngt
     *
     */
    public static enum TYPE_STATE implements StateChangeEventType{
        ENTER_TYPE,
        EXIT_TYPE
    }
    
    @Override
    public StateChangeEventType getEnterEventType() {
        return TYPE_STATE.ENTER_TYPE;
    }

    @Override
    public StateChangeEventType getExitEventType() {
        return TYPE_STATE.EXIT_TYPE;
    }

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return event.getToken().isTypeDescription();
    }

}
