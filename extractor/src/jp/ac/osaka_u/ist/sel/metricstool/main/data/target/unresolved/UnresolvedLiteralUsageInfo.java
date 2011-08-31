package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���������e�����g�p��\���N���X
 * 
 * @author higo
 *
 */
public final class UnresolvedLiteralUsageInfo extends UnresolvedExpressionInfo<LiteralUsageInfo> {

    /**
     * ���e�����̕�����\���ƃ��e�����̌^��^���ăI�u�W�F�N�g��������
     * 
     * @param literal ���e�����̕�����\��
     * @param type ���e�����̌^
     */
    public UnresolvedLiteralUsageInfo(final String literal, final TypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == literal) || (null == type)) {
            throw new IllegalArgumentException();
        }

        this.literal = literal;
        this.type = type;
        this.resolvedInfo = null;
    }

    @Override
    public LiteralUsageInfo resolve(final TargetClassInfo usingClass,
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

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final String literal = this.getLiteral();
        final UnresolvedTypeInfo<?> unresolvedType = this.getType();
        final TypeInfo type = unresolvedType.resolve(usingClass, usingMethod, classInfoManager,
                fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new LiteralUsageInfo(literal, type, usingMethod, fromLine, fromColumn,
                toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * ���̃��e�����g�p�̕������Ԃ�
     * 
     * @return ���̃��e�����g�p�̕�����
     */
    public final String getLiteral() {
        return this.literal;
    }

    /**
     * ���̃��e�����g�p�̌^�Ԃ�
     * 
     * @return�@���̃��e�����g�p�̌^��Ԃ�
     */
    public final UnresolvedTypeInfo<?> getType() {

        if (this.type instanceof PrimitiveTypeInfo) {
            return (PrimitiveTypeInfo) this.type;
        }

        final String[] fqname = ((ClassTypeInfo) this.type).getReferencedClass()
                .getFullQualifiedName();
        return new UnresolvedClassTypeInfo(fqname);
    }

    private final String literal;

    private final TypeInfo type;

}
