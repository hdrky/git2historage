package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �L���X�g�̎g�p��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class CastUsageInfo extends ExpressionInfo {

    /**
     * �K�v�ȏ���^���ăI�u�W�F�N�g��������
     * 
     * @param castType �L���X�g�̌^
     * @param castedUsage �L���X�g�����v�f
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public CastUsageInfo(final TypeInfo castType, final ExpressionInfo castedUsage,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == castType || null == castedUsage) {
            throw new IllegalArgumentException();
        }

        this.castType = castType;
        this.castedUsage = castedUsage;

        this.castedUsage.setOwnerExecutableElement(this);
    }

    /**
     * ���̃L���X�g�̌^��Ԃ�
     * 
     * @return ���̃L���X�g�̌^
     */
    @Override
    public TypeInfo getType() {
        return this.castType;
    }

    /**
     * �L���X�g�����v�f��Ԃ�
     * 
     * @return �L���X�g�����v�f
     */
    public ExpressionInfo getCastedUsage() {
        return this.castedUsage;
    }

    /**
     * ���̎��i�L���X�g�g�p�j�ɂ�����ϐ����p�̈ꗗ��Ԃ�
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getCastedUsage().getVariableUsages();
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getCastedUsage().getCalls();
    }

    /**
     * ���̃L���X�g�g�p�̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̃L���X�g�g�p�̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("(");

        final TypeInfo type = this.getType();
        sb.append(type.getTypeName());

        sb.append(")");

        final ExpressionInfo expression = this.getCastedUsage();
        sb.append(expression.getText());

        return sb.toString();
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getCastedUsage().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {

        final TypeInfo castType = this.getType();
        final ExpressionInfo castedUsage = (ExpressionInfo) this.getCastedUsage().copy();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final CastUsageInfo newCastUsage = new CastUsageInfo(castType, castedUsage, ownerMethod,
                fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newCastUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newCastUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newCastUsage;
    }

    private final TypeInfo castType;

    private final ExpressionInfo castedUsage;
}
