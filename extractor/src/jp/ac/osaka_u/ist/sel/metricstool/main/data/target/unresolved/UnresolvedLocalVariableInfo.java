package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ���[�J���ϐ���\�����߂̃N���X�D �ȉ��̏������D
 * <ul>
 * <li>�ϐ���</li>
 * <li>�������^��</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public final class UnresolvedLocalVariableInfo
        extends
        UnresolvedVariableInfo<LocalVariableInfo, UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>> {

    /**
     * ���[�J���ϐ��u�W�F�N�g������������D
     * 
     * @param name �ϐ���
     * @param type �������^��
     * @param definitionSpace �錾���Ă��郍�[�J�����
     * @param initializer ���[�J���ϐ��̏�������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedLocalVariableInfo(final String name, final UnresolvedTypeInfo<?> type,
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> definitionSpace,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializer,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(name, type, definitionSpace, fromLine, fromColumn, toLine, toColumn);

        this.initializer = initializer;
    }

    /**
     * ���������[�J���ϐ������������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass ���������[�J���ϐ��̒�`���s���Ă���N���X
     * @param usingMethod ���������[�J���ϐ��̒�`���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς݃��[�J���ϐ����
     */
    @Override
    public LocalVariableInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �C���q�C�ϐ����C�^���擾
        final Set<ModifierInfo> localModifiers = this.getModifiers();
        final String variableName = this.getName();
        final UnresolvedTypeInfo<?> unresolvedVariableType = this.getType();
        TypeInfo variableType = unresolvedVariableType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert variableType != null : "resolveTypeInfo returned null!";
        if (variableType instanceof UnknownTypeInfo) {
            if (unresolvedVariableType instanceof UnresolvedClassReferenceInfo) {

                final ExternalClassInfo externalClass = UnresolvedClassReferenceInfo
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedVariableType);
                variableType = new ClassTypeInfo(externalClass);
                for (final UnresolvedTypeInfo<?> unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedVariableType)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    ((ClassTypeInfo) variableType).addTypeArgument(typeArgument);
                }
                classInfoManager.add(externalClass);

            } else if (unresolvedVariableType instanceof UnresolvedArrayTypeInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final UnresolvedTypeInfo<?> unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedVariableType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedVariableType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolve(usingClass, usingMethod,
                        classInfoManager, fieldInfoManager, methodInfoManager);
                variableType = ArrayTypeInfo.getType(elementType, dimension);

            } else {
                assert false : "Can't resolve method local variable type : "
                        + unresolvedVariableType.toString();
            }
        }
        final int localFromLine = this.getFromLine();
        final int localFromColumn = this.getFromColumn();
        final int localToLine = this.getToLine();
        final int localToColumn = this.getToColumn();

        final LocalSpaceInfo definitionSpace = this.getDefinitionUnit().resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // ���[�J���ϐ��I�u�W�F�N�g�𐶐����CMethodInfo�ɒǉ�
        this.resolvedInfo = new LocalVariableInfo(localModifiers, variableName, variableType,
                definitionSpace, localFromLine, localFromColumn, localToLine, localToColumn);
        return this.resolvedInfo;
    }

    /**
     * �ϐ��̏���������Ԃ�
     * @return �ϐ��̏��������D����������Ă��Ȃ��ꍇ��null
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getInitilizer() {
        return this.initializer;
    }

    /**
     * �ϐ��̏���������\���ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> initializer;
}
