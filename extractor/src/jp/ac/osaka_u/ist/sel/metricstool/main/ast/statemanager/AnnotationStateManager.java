package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ArrayInitializerStateManager.ARRAY_INITILIZER_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ArrayInitializerStateManager.STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ExpressionStateManager.EXPR_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �A�m�e�[�V�����̏�Ԃ�\���X�e�[�g�}�l�[�W��
 * @author a-saitoh
 *
 */
public class AnnotationStateManager extends
        StackedAstVisitStateManager<AnnotationStateManager.STATE> {

    public AnnotationStateManager() {
        this.setState(STATE.OUT);
    }

    public static enum ANNOTATION_STATE implements StateChangeEventType {
        ENTER_ANNOTATION, ENTER_ANNOTATION_STRING, EXIT_ANNOTATION, EXIT_ANNOTATION_STRING,

        /* �^�ʖڂɃA�m�e�[�V��������͂����K�NANNOTATION_STRING�̑���ɂ����̏�Ԃ��g��
        EXIT_ANNOTATION_MEMBER, EXIT_ANNOTATION_MEMBER_VALUE_PAIR,
        EXIT_ANNOTATION_ARRAY_INIT,ENTER_ANNOTATION_MEMBER, ENTER_ANNOTATION_MEMBER_VALUE_PAIR,
        ENTER_ANNOTATION_ARRAY_INIT,
        */
    }

    protected static enum STATE {
        OUT, IN_ANNOTATION
    }

    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        final AstToken token = event.getToken();
        if (token.isAnnotation()) {
            this.setState(STATE.IN_ANNOTATION);
            this.fireStateChangeEvent(ANNOTATION_STATE.ENTER_ANNOTATION, event);
        } else if (this.isInAnnotation() && token.isAnnotationString()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.ENTER_ANNOTATION_STRING, event);
        }/* �^�ʖڂɃA�m�e�[�V��������͂���ꍇ�͂�������g��
         else if (this.isInAnnotation() && token.isAnnotationMember()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.ENTER_ANNOTATION_MEMBER, event);
         } else if (this.isInAnnotation() && token.isAnnotationMemberValuePair()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.ENTER_ANNOTATION_MEMBER_VALUE_PAIR, event);
         } else if (this.isInAnnotation() && token.isAnnotationArrayInit()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.ENTER_ANNOTATION_ARRAY_INIT, event);
         }
         */
    }

    @Override
    public void exited(final AstVisitEvent event) {
        super.exited(event);

        final AstToken token = event.getToken();
        if (token.isAnnotation()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.EXIT_ANNOTATION, event);
            this.setState(STATE.OUT);
        } else if (this.isInAnnotation() && token.isAnnotationString()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.EXIT_ANNOTATION_STRING, event);
        }
        /* �^�ʖڂɃA�m�e�[�V��������͂���ꍇ�͂�������g��
          else if (this.isInAnnotation() && token.isAnnotationMember()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.EXIT_ANNOTATION_MEMBER, event);
        } else if (this.isInAnnotation() && token.isAnnotationMemberValuePair()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.EXIT_ANNOTATION_MEMBER_VALUE_PAIR, event);
        } else if (this.isInAnnotation() && token.isAnnotationArrayInit()) {
            this.fireStateChangeEvent(ANNOTATION_STATE.EXIT_ANNOTATION_ARRAY_INIT, event);
        } 
        */
    }

    @Override
    protected boolean isStateChangeTriggerEvent(AstVisitEvent event) {
        final AstToken token = event.getToken();
        return token.isAnnotation();
    }

    private boolean isInAnnotation() {
        return this.getState().equals(STATE.IN_ANNOTATION);
    }

}
