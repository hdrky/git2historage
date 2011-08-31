package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �r�W�^�[�����L�q���ɓ��B�������ɏ�ԑJ�ڂ��C��ԑJ�ڃC�x���g��ʒm����D
 * ���̃N���X�͎�Ɏ���͕��̃r���_�[�̗L��������؂�ւ��邽�߂Ɏg�p����邱�Ƃ�z�肵�Ă���D
 * <p>
 * ���L�q�����Ɏ����p�����Ȃ��c���[�iJava�̓����N���X�錾�Ȃǁj�����݂���ꍇ�́C���̓����͍\��������̂ł͂Ȃ��Ɣ��肵�C
 * ���L�q������o����ԂɑJ�ڂ���D
 * �����āC���̃c���[�̖K�₪�I��������C������x���L�q���ɓ�������ԂɑJ�ڂ���D
 * 
 * @author kou-tngt
 *
 */
public class ExpressionStateManager extends
        StackedAstVisitStateManager<ExpressionStateManager.STATE> {

    public ExpressionStateManager() {
        this.setState(STATE.NOT);
    }
    
    /**
     * �ʒm�����ԑJ�ڃC�x���g�̃C�x���g�^�C�v��\��Enum
     * @author kou-tngt
     *
     */
    public static enum EXPR_STATE implements StateChangeEventType {
        ENTER_EXPR, EXIT_EXPR
    }

    /**
     * �r�W�^�[��AST�m�[�h�̒��ɓ��������̃C�x���g�ʒm���󂯎��C
     * ���̃m�[�h�����L�q���⎮���p�����Ȃ��m�[�h�ł���΁C
     * ��Ԃ�J�ڂ�������C�x���g�𔭍s����
     * 
     * @param event AST�r�W�b�g�C�x���g
     */
    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        final AstToken token = event.getToken();
        if (token.isExpression()) {
            this.setState(STATE.IN);
            this.fireStateChangeEvent(EXPR_STATE.ENTER_EXPR, event);
        } else if (this.isExpressionInsulator(token)) {
            this.setState(STATE.NOT);
            this.fireStateChangeEvent(EXPR_STATE.EXIT_EXPR, event);
        }
    }

    /**
     * �r�W�^�[��AST�m�[�h�̂���o�����̃C�x���g�ʒm���󂯎��C
     * ���̃m�[�h�����L�q���⎮���p�����Ȃ��m�[�h�ł���΁C
     * ��Ԃ�߂�����C�x���g�𔭍s����
     * 
     * @param event AST�r�W�b�g�C�x���g
     */
    @Override
    public void exited(final AstVisitEvent event) {
        super.exited(event);

        final AstToken token = event.getToken();
        if (token.isExpression()) {
            this.fireStateChangeEvent(EXPR_STATE.EXIT_EXPR, event);
        } else if (this.isExpressionInsulator(token) && STATE.IN == this.getState()) {
            this.fireStateChangeEvent(EXPR_STATE.ENTER_EXPR, event);
        }
    }

    /**
     * ���̒��ɂ��邩�ǂ�����Ԃ����\�b�h
     * @return�@���̒��ɋ���ꍇ��true
     */
    public boolean inExpression() {
        return STATE.IN == this.getState();
    }

    /**
     * �����ŗ^����ꂽ�g�[�N�������̌p�����Ȃ��m�[�h���ǂ�����Ԃ��D
     * �f�t�H���g�����ł́Ctoken.isBlock()��true��Ԃ���true��Ԃ��D
     * ���̃��\�b�h���I�[�o�[���[�h���邱�ƂŁC�C�ӂ̃m�[�h�Ŏ�����؂�悤�ȏ�ԑJ�ڂ�����N���X���쐬���邱�Ƃ��ł���D
     * 
     * @param token ���̌p�����Ȃ��m�[�h���ǂ�����Ԃ��g�[�N��
     * @return ���̌p�����Ȃ��m�[�h�ł����true
     */
    protected boolean isExpressionInsulator(final AstToken token) {
        return token.isBlock();
    }

    /**
     * �����ŗ^����ꂽ�g�[�N������ԕω��̃g���K�ɂȂ蓾�邩�ǂ�����Ԃ�.
     * token.isExpression() �܂��� {@link #isExpressionInsulator(AstToken)}�̂ǂ��炩�𖞂�����
     * true��Ԃ��D
     * @param token ��ԕω��̃g���K�ƂȂ蓾�邩�ǂ����𒲂ׂ�g�[�N��
     * @return token.isExpression() �܂��� {@link #isExpressionInsulator(AstToken)}�̂ǂ��炩�𖞂����ꍇtrue
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        AstToken token = event.getToken();
        return token.isExpression() || this.isExpressionInsulator(token);
    }

    /**
     * ��Ԃ�\��Enum
     * @author kou-tngt
     *
     */
    protected static enum STATE {
        NOT, IN,
    }


}
