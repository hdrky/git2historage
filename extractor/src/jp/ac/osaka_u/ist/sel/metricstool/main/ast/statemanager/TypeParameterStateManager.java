package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �r�W�^�[���^�p�����[�^��`���֏o���肷��ۂ̏�Ԃ��Ǘ�����X�e�[�g�}�l�[�W���D
 * @author kou-tngt
 *
 */
public class TypeParameterStateManager extends
        StackedAstVisitStateManager<TypeParameterStateManager.STATE> {

    public TypeParameterStateManager() {
        this.setState(STATE.OUT);
    }

    /**
     * ���M�����ԕω��C�x���g�̎�ނ�\��enum
     * @author kou-tngt
     *
     */
    public enum TYPE_PARAMETER implements StateChangeEventType {
        ENTER_TYPE_PARAMETER_DEF, EXIT_TYPE_PARAMETER_DEF, ENTER_TYPE_UPPER_BOUNDS, EXIT_TYPE_UPPER_BOUNDS, ENTER_TYPE_LOWER_BOUNDS, EXIT_TYPE_LOWER_BOUNDS,
        ENTER_TYPE_ADDITIONAL_BOUNDS, EXIT_TYPE_ADDITIONAL_BOUNDS
    }

    /**
     * �^�p�����[�^��`���̃m�[�h�ɓ��������ɌĂяo����C
     * ��ԕω��������N�����āC��ԕω��C�x���g��ʒm����D
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(AstVisitEvent event) {
        super.entered(event);

        AstToken token = event.getToken();

        if (token.isTypeParameterDefinition()) {
            this.setState(STATE.IN_PARAMETER_DEF);
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF, event);
        } else if (token.isTypeLowerBoundsDescription()) {
            this.setState(STATE.IN_LOWER_BOUNDS);
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS, event);
        } else if (token.isTypeUpperBoundsDescription()) {
            this.setState(STATE.IN_UPPER_BOUNDS);
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS, event);
        } else if (token.isTypeAdditionalBoundsDescription()){
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_ADDITIONAL_BOUNDS, event);
        }
            
    }

    /**
     * �^�p�����[�^��`���̃m�[�h����o�����ɌĂяo����C
     * ��ԕω��������N�����āC��ԕω��C�x���g��ʒm����D
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(AstVisitEvent event) {
        super.exited(event);

        AstToken token = event.getToken();

        if (token.isTypeParameterDefinition()) {
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF, event);
        } else if (token.isTypeLowerBoundsDescription()) {
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS, event);
        } else if (token.isTypeUpperBoundsDescription()) {
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS, event);
        } else if (token.isTypeAdditionalBoundsDescription()){
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_ADDITIONAL_BOUNDS, event);
        }
    }

    /**
     * �r�W�^�[�̌��݈ʒu���^�p�����[�^��`���̒����ǂ�����Ԃ�
     * @return�@�r�W�^�[�̌��݈ʒu���^�p�����[�^��`���̒��ł����true
     */
    public boolean isEnterParameterDefinition() {
        return STATE.OUT != this.getState();
    }

    /**
     * �r�W�^�[�̌��݈ʒu���^�p�����[�^��`��(��E�錾���ɓ���O)�̒����ǂ�����Ԃ�
     * @return�@�r�W�^�[�̌��݈ʒu���^�p�����[�^��`��(��E�錾���ɓ���O)�̒��ł����true
     */
    public boolean isInTypeParameterDefinition(){
        return STATE.IN_PARAMETER_DEF == this.getState();
    }
    

    /**
     * �^�p�����[�^��`���Ɋ֘A����m�[�h���ǂ����𔻒肷��
     *  
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#isStateChangeTriggerEvent(jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken)
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        AstToken token = event.getToken();
        return token.isTypeParameterDefinition() || token.isTypeLowerBoundsDescription()
                || token.isTypeUpperBoundsDescription();
    }

    /**
     * ��Ԃ�\��enum
     * 
     * @author kou-tngt
     *
     */
    protected enum STATE {
        OUT, IN_PARAMETER_DEF, IN_UPPER_BOUNDS, IN_LOWER_BOUNDS
    }
    

}
