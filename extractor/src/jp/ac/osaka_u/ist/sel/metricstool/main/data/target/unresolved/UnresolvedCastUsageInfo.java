package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������L���X�g�g�p��\���N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedCastUsageInfo extends UnresolvedExpressionInfo<CastUsageInfo> {

    /**
     * �L���X�g���ꂽ�G���e�B�e�B�ƃL���X�g�̌^��^���ď�����
     * 
     * @param castType �L���X�g�̌^
     * @param castedUsage �L���X�g���ꂽ�G���e�B�e�B
     * 
     */
    public UnresolvedCastUsageInfo(final UnresolvedTypeInfo<?> castType,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> castedUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == castType || null == castedUsage) {
            throw new IllegalArgumentException();
        }

        this.castType = castType;
        this.castedUsage = castedUsage;
    }

    /**
     * �L���X�g�����^��Ԃ�
     * @return �L���X�g�����^
     */
    public UnresolvedTypeInfo<?> getCastType() {
        return this.castType;
    }

    /**
     * �L���X�g���s��ꂽ�G���e�B�e�B�g�p��Ԃ�
     * @return �L���X�g���s��ꂽ�G���e�B�e�B�g�p
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getCastedUsage() {
        return this.castedUsage;
    }

    @Override
    public CastUsageInfo resolve(final TargetClassInfo usingClass,
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

        // �L���X�g�^�g�p������
        final UnresolvedTypeInfo<?> unresolvedCastType = this.getCastType();
        final TypeInfo castType = unresolvedCastType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // �L���X�g���ꂽ�G���e�B�e�B�g�p������
        final ExpressionInfo castedUsage = this.getCastedUsage().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
        /*final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        // �L���X�g�g�p������
        this.resolvedInfo = new CastUsageInfo(castType, castedUsage, usingMethod, fromLine,
                fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/

        return this.resolvedInfo;
    }

    /**
     * �L���X�g�����^��ۑ�����ϐ�
     */
    private final UnresolvedTypeInfo<?> castType;

    /**
     * �L���X�g���s��ꂽ�G���e�B�e�B�g�p��ۑ������߂̕ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> castedUsage;

}
