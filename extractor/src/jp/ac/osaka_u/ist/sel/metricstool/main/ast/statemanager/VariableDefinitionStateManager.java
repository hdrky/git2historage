package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaAstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * AST�r�W�^�[�����炩�̕ϐ���`���Ƃ��̏��������ɓ��B�������ɏ�ԑJ�ڂ��N�����X�e�[�g�}�V�����������钊�ۃN���X.
 * 
 * �T�u�N���X��{@link #isDefinitionToken(AstToken)}���\�b�h���������C�Ή�����ϐ���`���m�[�h��\���g�[�N�����w�肵�Ȃ���΂Ȃ�Ȃ�.
 * @author kou-tngt
 *
 */
public abstract class VariableDefinitionStateManager extends
        StackedAstVisitStateManager<VariableDefinitionStateManager.STATE> {

    public VariableDefinitionStateManager() {
        this.setState(STATE.OUT);
    }

    /**
     * ��ԕω��̎�ނ�\��enum
     * 
     * @author kou-tngt
     *
     */
    public static enum VARIABLE_STATE implements StateChangeEventType {
        ENTER_VARIABLE_DEF, ENTER_VARIABLE_INITIALIZER, EXIT_VARIABLE_INITIALIZER, EXIT_VARIABLE_DEF;
    }

    /**
     * �r�W�^�[��AST�m�[�h�̒��ɓ��������̃C�x���g�ʒm���󂯎��C
     * 
     * @param event �C�x���g
     */
    @Override
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (isStateChangeTriggerEvent(event)) {
            //���݂̏�Ԃ�ۑ�
            super.entered(event);

            if (this.isDefinitionToken(token)) {
                //��`���ɓ������̂ŏ�ԑJ�ڂ����ăC�x���g�𔭍s����
                this.setState(STATE.DEFINITION);
                this.fireStateChangeEvent(VARIABLE_STATE.ENTER_VARIABLE_DEF, event);
            } /*else if (this.isInDefinition() && token.isAnnotation()) {
                System.out.println("annotation");
              }*/

            else if (token.isAssignmentOperator() && STATE.DEFINITION == this.getState()) {
                //�A�m�e�[�V�������̑�����Z�q�͖���
                if (!event.getParentToken().isAnnotationString()) {
                    //��`�����ɑ�����Z�q���������̂ŁC���������ւƏ�ԑJ�ڂ����ăC�x���g�𔭍s����                    
                    this.setState(STATE.INITIALIZER);
                    this.fireStateChangeEvent(VARIABLE_STATE.ENTER_VARIABLE_INITIALIZER, event);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (isStateChangeTriggerEvent(event)) {
            //��Ԃ𕜌�
            super.exited(event);

            if (this.isDefinitionToken(token)) {
                //��`������o���̂ŃC�x���g�𔭍s����
                this.fireStateChangeEvent(VARIABLE_STATE.EXIT_VARIABLE_DEF, event);
            } else if (token.isAssignmentOperator() && STATE.DEFINITION == this.getState()) {
                //�A�m�e�[�V�������̑�����Z�q�͖���
                if (!event.getParentToken().isAnnotationString()) {
                    //������������o���̂ŃC�x���g�𔭍s����
                    this.fireStateChangeEvent(VARIABLE_STATE.EXIT_VARIABLE_INITIALIZER, event);
                }
            }
        }
    }

    /**
     * �r�W�^�[���Ή�����ϐ���`���ɂ��邩�ǂ�����Ԃ�.
     * @return �ϐ���`���ɂ���ꍇ��true
     */
    public boolean isInDefinition() {
        return STATE.DEFINITION == this.getState();
    }

    /**
     * �r�W�^�[���Ή�����ϐ����������ɂ��邩�ǂ�����Ԃ�.
     * @return�@�ϐ����������ɂ���ꍇ��true
     */
    public boolean isInInitializer() {
        return STATE.INITIALIZER == this.getState();
    }

    /**
     * �g�[�N�����Ή�����ϐ���`�����ǂ�����Ԃ����ۃ��\�b�h�D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�T�u�N���X���Ή�����ϐ��̎�ނ��w�肷�邱�Ƃ��ł���D
     * 
     * @param token�@�ϐ���`�����ǂ������肷��g�[�N��
     * @return �ϐ���`���ł����true.
     */
    protected abstract boolean isDefinitionToken(AstToken token);

    /**
     * ������Z�q�C�܂��͕ϐ���`����\���g�[�N�����ǂ�����Ԃ�.
     * 
     * @param ������Z�q�C�܂��͕ϐ���`����\���g�[�N�����ǂ����𔻒肷��g�[�N��
     * @return ������Z�q�C�܂��͕ϐ���`����\���g�[�N���ł����true��Ԃ�
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#isStateChangeTriggerEvent(jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken)
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        AstToken token = event.getToken();
        return token.isAssignmentOperator() || this.isDefinitionToken(token);
        // || token.isAnnotation();
    }

    /**
     * ��Ԃ�\��enum
     * @author kou-tngt
     */
    protected static enum STATE {
        OUT, DEFINITION, INITIALIZER
    }

}
