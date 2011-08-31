package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ExpressionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParenthesesExpressionInfo;


/**
 * ���ʎ������\�z����N���X 
 * <br>
 * �����\������v�f�ł͂��邪�C���̍\���؏��ɉe����^���Ă͂����Ȃ����߁C 
 * {@link ExpressionBuilder}�̎q�N���X�ɂ͂��Ă��Ȃ��D
 * 
 * @author g-yamada
 * 
 */
public class ParenthesesExpressionBuilder extends
        StateDrivenDataBuilder<UnresolvedParenthesesExpressionInfo> {

    /**
     * �I�u�W�F�N�g������������
     * 
     * @param expressionManager ExpressionElementManager
     * @param buildDataManager 
     */
    public ParenthesesExpressionBuilder(final ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        if (null == buildDataManager || null == expressionManager) {
            throw new IllegalArgumentException();
        }

        this.buildDataManager = buildDataManager;
        this.expressionManager = expressionManager;

        this.addStateManager(this.expressionStateManger);
    }

    @Override
    public void entered(final AstVisitEvent e) {
        super.entered(e);
    }

    /**
     * exit �����m�[�h�����ʎ��Ȃ�CUnresolvedParenthesesExpressionInfo�����閽�߂�����
     */
    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        super.exited(e);
        final AstToken token = e.getToken();
        if (this.isActive() && this.expressionStateManger.inExpression()
                && token.isParenthesesExpression()) {
            this.buildParenthesesExpressionBuilder(e);
        }
    }

    /**
     * ���߂���Ď��ۂ�UnresolvedParenthesesExpressionInfo������
     */
    protected void buildParenthesesExpressionBuilder(final AstVisitEvent e) {
        final ExpressionElement parentheticElement = expressionManager.popExpressionElement();
        final UnresolvedExpressionInfo<? extends ExpressionInfo> parentheticExpression = parentheticElement
                .getUsage();

        if (null != parentheticExpression) {
            // expressionAnalyzeStack�̓��̗v�f���|�b�v���āC�����Ɋ��ʎ����v�b�V������
            final UnresolvedParenthesesExpressionInfo paren = new UnresolvedParenthesesExpressionInfo(
                    parentheticExpression);
            paren.setOuterUnit(this.buildDataManager.getCurrentUnit());
            paren.setFromLine(e.getStartLine());
            paren.setFromColumn(e.getStartColumn());
            paren.setToLine(e.getEndLine());
            paren.setToColumn(e.getEndColumn());
            expressionManager.pushExpressionElement(new UsageElement(paren));
        } else if (parentheticElement instanceof IdentifierElement) {
            // (a) �̂悤�Ɏ��ʎq�݂̂��͂ފ��ʂ̏ꍇ�CUsage���������ł��邽�ߊ���Element�����push����
            expressionManager.pushExpressionElement(new ParenthesizedIdentifierElement(
                    (IdentifierElement) parentheticElement, e.getStartLine(), e.getStartColumn(), e
                            .getEndLine(), e.getEndColumn()));
        } else {
            // here shouldn't be reached
            throw new IllegalStateException();
        }
    }

    //public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveAsVariableIfPoss

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        // nothing to do
    }

    protected final ExpressionElementManager expressionManager;

    protected final BuildDataManager buildDataManager;

    private final ExpressionStateManager expressionStateManger = new ExpressionStateManager();
}