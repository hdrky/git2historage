package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������ϐ��錾����\���N���X
 * 
 * @author higo
 *
 */
public final class UnresolvedVariableDeclarationStatementInfo extends
        UnresolvedSingleStatementInfo<VariableDeclarationStatementInfo> implements
        UnresolvedConditionInfo<VariableDeclarationStatementInfo> {

    /**
     * �錾����Ă���ϐ��C�i��������΁j�������̎���^���āC�I�u�W�F�N�g��������
     * 
     * @param variableDeclaration �錾����Ă���ϐ�
     * @param initializationExpression �i��������΁j�������̎�
     */
    public UnresolvedVariableDeclarationStatementInfo(
            final UnresolvedLocalVariableUsageInfo variableDeclaration,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression) {

        super(variableDeclaration.getUsedVariable().getDefinitionUnit());

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == variableDeclaration) {
            throw new IllegalArgumentException("declaredVariable is null");
        }
        this.variableDeclaration = variableDeclaration;
        this.initializationExpression = initializationExpression;
        this.setOuterUnit(variableDeclaration.getOuterUnit());
    }

    @Override
    public VariableDeclarationStatementInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnresolvedUnitInfo<? extends UnitInfo> unresolvedOuterUnit = this.getOuterUnit();
        final LocalSpaceInfo outerLocalSpace = (LocalSpaceInfo) unresolvedOuterUnit.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final LocalVariableUsageInfo variableDeclaration = this.variableDeclaration.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final ExpressionInfo initializationExpression = null != this.initializationExpression ? this.initializationExpression
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager)
                : null;
        this.resolvedInfo = new VariableDeclarationStatementInfo(outerLocalSpace, variableDeclaration,
                initializationExpression, fromLine, fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * ��`����Ă���ϐ���Ԃ�
     * 
     * @return ��`����Ă���ϐ�
     */
    public final UnresolvedLocalVariableInfo getDeclaredLocalVariable() {
        return this.variableDeclaration.getUsedVariable();
    }

    /**
     * �錾����Ă���ϐ��̏���������Ԃ�
     * 
     * @return �錾����Ă���ϐ��̏��������D����������Ă��ꍇ��null
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * �錾����Ă���ϐ�������������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �錾����Ă���ϐ�������������Ă����true
     */
    public boolean isInitialized() {
        return null != this.initializationExpression;
    }

    @Override
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit() {
        final UnresolvedLocalSpaceInfo<?> outerUnit = (UnresolvedLocalSpaceInfo<?>) this
                .getOuterUnit();
        return outerUnit instanceof UnresolvedCallableUnitInfo<?> ? (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) outerUnit
                : outerUnit.getOuterCallableUnit();
    }

    @Override
    public UnresolvedClassInfo getOuterClass() {
        return this.getOuterCallableUnit().getOuterClass();
    }

    /**
     * �錾����Ă���ϐ���\���t�B�[���h
     */
    private final UnresolvedLocalVariableUsageInfo variableDeclaration;

    /**
     * �錾����Ă���ϐ��̏���������\���t�B�[���h
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression;
}
