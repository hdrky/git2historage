package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * �N���X�̎Q�Ƃ�\���N���X�D
 * ReferenceTypeInfo�@�́u�Q�ƌ^�v��\���̂ɑ΂��āC
 * ���̃N���X�̓N���X�̎Q�ƂɊւ�����i�Q�ƈʒu�Ȃǁj��\��
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ClassReferenceInfo extends ExpressionInfo {

    /**
     * �Q�ƌ^��^���ăI�u�W�F�N�g��������
     * 
     * @param referenceType ���̃N���X�Q�Ƃ̎Q�ƌ^
     * @param ownerMethod �I�[�i�[���\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ClassReferenceInfo(final ClassTypeInfo referenceType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.referenceType = referenceType;
    }

    /**
     * ���̃N���X�Q�Ƃ̎Q�ƌ^��Ԃ�
     * 
     * @return ���̃N���X�Q�Ƃ̎Q�ƌ^
     */
    @Override
    public TypeInfo getType() {
        return this.referenceType;
    }

    /**
     * ���̃N���X�Q�ƂŎQ�Ƃ���Ă���N���X��Ԃ�
     * 
     * @return ���̃N���X�Q�ƂŎQ�Ƃ���Ă���N���X
     */
    public ClassInfo getReferencedClass() {
        return this.referenceType.getReferencedClass();
    }

    /**
     * �N���X�Q�Ƃɂ����ĕϐ����g�p����邱�Ƃ͂Ȃ��̂ŋ�̃Z�b�g��Ԃ�
     * 
     * @return ��̃Z�b�g
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * ���̃N���X�Q�Ƃ̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ���̃N���X�Q�Ƃ̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {

        final ClassInfo classInfo = this.getReferencedClass();
        return classInfo.getFullQualifiedName(".");
    }

    /**
     * ���̎��œ�������\���������O��Set��Ԃ�
     * 
     * @return�@���̎��œ�������\���������O��Set
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    @Override
    public ExecutableElementInfo copy() {
        final ClassTypeInfo classType = (ClassTypeInfo) this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ClassReferenceInfo newClassReference = new ClassReferenceInfo(classType, ownerMethod,
                fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newClassReference.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newClassReference.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newClassReference;

    }

    /**
     * ���̃N���X�Q�Ƃ̎Q�ƌ^��ۑ�����ϐ�
     */
    private final ClassTypeInfo referenceType;
}
