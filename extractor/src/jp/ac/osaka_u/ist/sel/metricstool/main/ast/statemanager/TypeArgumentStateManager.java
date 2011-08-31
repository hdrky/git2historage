package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �^�Q�Ƃ⃁�\�b�h�Ăяo�����ɗ^����ꂽ�^������\���m�[�h�ɁC
 * AST�r�W�^�[�������Ă������̏�ԊǗ����s���D
 * 
 * @author kou-tngt
 *
 */
public class TypeArgumentStateManager extends
        StackedAstVisitStateManager<TypeArgumentStateManager.STATE> {

    public TypeArgumentStateManager() {
        this.setState(STATE.OUT);
    }

    /**
     * �ʒm����C�x���g�̃^�C�v��\��enum
     * @author kou-tngt
     *
     */
    public static enum TYPE_ARGUMENT_STATE implements StateChangeEventType {
        ENTER_TYPE_ARGUMENTS, EXIT_TYPE_ARGUMENTS, ENTER_TYPE_ARGUMENT, EXIT_TYPE_ARGUMENT, ENTER_TYPE_WILDCARD, EXIT_TYPE_WILDCARD,
    }

    /**
     * �r�W�^�[���m�[�h�ɓ��������̃C�x���g���󂯎��C
     * ���̃m�[�h���^�����Ɋ֘A������̂ł���Ώ�ԑJ�ڂ��s���ăC�x���g�𔭍s����D
     * @param �r�W�^�[���ʒm����AST�K��C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        final AstToken token = event.getToken();

        if (token.isTypeArgument()) {
            this.setState(STATE.IN_ARGUMENT);
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.ENTER_TYPE_ARGUMENT, event);
        } else if (token.isTypeArguments()) {
            this.setState(STATE.IN_ARGUMENTS);
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.ENTER_TYPE_ARGUMENTS, event);
        } else if (token.isTypeWildcard()) {
            this.setState(STATE.IN_WILDCARD);
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.ENTER_TYPE_WILDCARD, event);
        }
    }

    /**
     * �r�W�^�[���m�[�h����o�����̃C�x���g���󂯎��C
     * ���̃m�[�h���^�����Ɋ֘A������̂ł���Ώ�ԑJ�ڂ��s���ăC�x���g�𔭍s����D
     * @param �r�W�^�[���ʒm����AST�K��C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent event) {
        super.exited(event);

        final AstToken token = event.getToken();

        if (token.isTypeArgument()) {
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.EXIT_TYPE_ARGUMENT, event);
        } else if (token.isTypeArguments()) {
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.EXIT_TYPE_ARGUMENTS, event);
        } else if (token.isTypeWildcard()) {
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.EXIT_TYPE_WILDCARD, event);
        }
    }

    /**
     * ����event����ԑJ�ڂ̈������ɂȂ�C�x���g���ǂ�����Ԃ��D
     * @param event ��ԑJ�ڂ̈������ɂȂ邩�ǂ����𒲂ׂ����C�x���g
     * @return ��ԑJ�ڂ̈������ɂȂ�g�[�N���Ȃ�true
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#isStateChangeTriggerEvent(jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken)
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        AstToken token = event.getToken();
        return token.isTypeArgument() || token.isTypeArguments() || token.isTypeWildcard();
    }

    /**
     * ��Ԃ�\��enum
     * @author kou-tngt
     *
     */
    protected enum STATE {
        OUT, IN_ARGUMENTS, IN_ARGUMENT, IN_WILDCARD
    }

}
