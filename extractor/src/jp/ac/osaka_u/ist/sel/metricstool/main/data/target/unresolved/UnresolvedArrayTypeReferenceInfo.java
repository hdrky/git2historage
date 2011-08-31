package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������z��^�Q�Ƃ�\���N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedArrayTypeReferenceInfo extends
        UnresolvedExpressionInfo<ArrayTypeReferenceInfo> {

    /**
     * �Q�Ƃ���Ă��関�����z��^��^���ď�����
     * 
     * @param referencedType �Q�Ƃ���Ă��関�����z��^
     */
    public UnresolvedArrayTypeReferenceInfo(final UnresolvedArrayTypeInfo referencedType,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencedType) {
            throw new IllegalArgumentException("referencedType is null");
        }

        this.referencedType = referencedType;
        this.resolvedInfo = null;

        this.setOuterUnit(outerUnit);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    @Override
    public ArrayTypeReferenceInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //�@�ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �Q�Ƃ���Ă���z��^������
        final UnresolvedArrayTypeInfo unresolvedArrayType = this.getType();
        final ArrayTypeInfo arrayType = unresolvedArrayType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        /*// �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        this.resolvedInfo = new ArrayTypeReferenceInfo(arrayType, usingMethod, fromLine,
                fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/

        return this.resolvedInfo;
    }

    /**
     * �Q�Ƃ���Ă��関�����z��^��Ԃ�
     * @return �Q�Ƃ���Ă��関�����z��^
     */
    public UnresolvedArrayTypeInfo getType() {
        return this.referencedType;
    }

    /**
     * �Q�Ƃ���Ă��関�����z��^��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedArrayTypeInfo referencedType;

}
