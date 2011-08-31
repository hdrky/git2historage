package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStatementInfo;


/**
 * ���̏����\�z����N���X
 * �����ȊO�i�����Creturn���Cthrow���Cbreak���Ȃǁj�̏����\�z����D
 * 
 * @author t-miyake
 *
 * @param <T> �\�z����镶�̌^�CUnresolvedStatementInfo�̃T�u�N���X�łȂ���΂Ȃ�Ȃ��D
 */
public abstract class SingleStatementBuilder<T extends UnresolvedStatementInfo<? extends StatementInfo>>
        extends CompoundDataBuilder<T> {

    /**
     * �����}�l�[�W���[�C�\�z�ς݃f�[�^�}�l�[�W���[��^���ď�����
     * 
     * @param expressionManager �����}�l�[�W���[
     * @param buildDataManager �\�z�ς݃f�[�^�}�l�[�W���[
     */
    public SingleStatementBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {

        if (null == buildDataManager || null == expressionManager) {
            throw new IllegalArgumentException();
        }

        this.buildDataManager = buildDataManager;
        this.expressionManager = expressionManager;
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        
    }    

    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        super.exited(e);
        if (this.isTriggerToken(e.getToken())) {
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this.buildDataManager
                    .getCurrentLocalSpace();

            if (null != currentLocal) {
                final T singleStatement = this.buildStatement(currentLocal, e.getStartLine(), e
                        .getStartColumn(), e.getEndLine(), e.getEndColumn());

                assert singleStatement != null : "Illegal state: a single statement was not built";

                currentLocal.addStatement(singleStatement);

                this.registBuiltData(singleStatement);
            }
        }
    }

    /**
     * �ߋ��ɍ\�z���ꂽ�����̂����ŐV�̎�����Ԃ��D
     * 
     * @return �ߋ��\�z���ꂽ�����̂����ŐV�̎����
     */
    protected UnresolvedExpressionInfo<? extends ExpressionInfo> getLastBuiltExpression() {
        return null == this.expressionManager.getPeekExpressionElement() ? null
                : this.expressionManager.getPeekExpressionElement().getUsage();
    }

    /**
     * ���̏����\�z����D
     * 
     * @param fromLine ���̊J�n�s
     * @param fromColumn ���̊J�n��
     * @param toLine ���̏I���s
     * @param toColumn ���̏I����
     * @return
     */
    protected abstract T buildStatement(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn);

    /**
     * �����ŗ^����ꂽ�g�[�N�����\�z����镶��\���m�[�h�̃g�[�N���ł��邩�ǂ����Ԃ�
     * 
     * @param token �g�[�N��
     * @return �����ŗ^����ꂽ�g�[�N�����\�z����镶��\���m�[�h�̃g�[�N���ł����true
     */
    protected abstract boolean isTriggerToken(final AstToken token);

    /**
     * �\�z�ςݎ����}�l�[�W���[��\���t�B�[���h
     */
    protected final ExpressionElementManager expressionManager;

    /**
     * �\�z�ς݃f�[�^�}�l�[�W���[��\���t�B�[���h
     */
    protected final BuildDataManager buildDataManager;
}
