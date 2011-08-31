package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �Ăяo���\�ȒP��(���\�b�h��R���X�g���N�^)��\���N���X
 * 
 * @author higo
 */

@SuppressWarnings("serial")
public abstract class CallableUnitInfo extends LocalSpaceInfo implements Visualizable, Modifier,
        TypeParameterizable {

    /**
     * �I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q��Set
     * @param ownerClass ���L�N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    CallableUnitInfo(final Set<ModifierInfo> modifiers, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.parameters = new LinkedList<ParameterInfo>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.thrownExceptions = new LinkedList<ReferenceTypeInfo>();

        this.unresolvedUsage = new HashSet<UnresolvedExpressionInfo<?>>();

        this.callers = new TreeSet<CallableUnitInfo>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * ��`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return ��`���ꂽ�ϐ���Set
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.addAll(super.getDefinedVariables());
        definedVariables.addAll(this.getParameters());
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * ���\�b�h�Ԃ̏����̎��́C��`����Ă���N���X���l�����邽�߂ɒ�`���Ă���D
     */
    @Override
    final public int compareTo(final Position o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (o instanceof CallableUnitInfo) {

            final ClassInfo ownerClass = this.getOwnerClass();
            final ClassInfo correspondOwnerClass = ((CallableUnitInfo) o).getOwnerClass();
            final int classOrder = ownerClass.compareTo(correspondOwnerClass);
            if (classOrder != 0) {
                return classOrder;
            }
        }

        return super.compareTo(o);
    }

    /**
     * �Ăяo�����j�b�g�̃n�b�V���R�[�h��Ԃ�
     */
    @Override
    final public int hashCode() {
        return this.getFromLine() + this.getFromColumn() + this.getToLine() + this.getToColumn();
    }

    public int compareArgumentsTo(final CallableUnitInfo target) {
        // �����̌��Ŕ�r
        final int parameterNumber = this.getParameterNumber();
        final int correspondParameterNumber = target.getParameterNumber();
        if (parameterNumber < correspondParameterNumber) {
            return 1;
        } else if (parameterNumber > correspondParameterNumber) {
            return -1;
        } else {

            // �����̌^�Ŕ�r�D���������珇�ԂɁD
            final Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
            final Iterator<ParameterInfo> correspondParameterIterator = target.getParameters()
                    .iterator();
            while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                final ParameterInfo parameter = parameterIterator.next();
                final ParameterInfo correspondParameter = correspondParameterIterator.next();
                final String typeName = parameter.getType().getTypeName();
                final String correspondTypeName = correspondParameter.getType().getTypeName();
                final int typeOrder = typeName.compareTo(correspondTypeName);
                if (typeOrder != 0) {
                    return typeOrder;
                }
            }

            return 0;
        }
    }

    /**
     * ���̃I�u�W�F�N�g���C�����ŗ^����ꂽ�����g���ČĂяo�����Ƃ��ł��邩�ǂ����𔻒肷��D
     * 
     * @param actualParameters �������̃��X�g
     * @return �Ăяo����ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    boolean canCalledWith(final List<ExpressionInfo> actualParameters) {

        if (null == actualParameters) {
            throw new IllegalArgumentException();
        }

        final ExpressionInfo[] actualParameterArray = actualParameters
                .toArray(new ExpressionInfo[0]);
        final ParameterInfo[] dummyParameterArray = this.getParameters().toArray(
                new ParameterInfo[0]);
        int checkedActualIndex = -1;

        for (int index = 0; index < dummyParameterArray.length; index++) {

            final ParameterInfo dummyParameter = dummyParameterArray[index];
            final TypeInfo dummyType = dummyParameter.getType();

            //���������ϒ������̏ꍇ
            if (dummyParameter instanceof VariableLengthParameterInfo) {

                // TODO ���݂̂Ƃ�������Ȃ���OK�ɂ��Ă���
                return true;
            }

            // �ϒ������ȊO�̏ꍇ
            else {

                // �������̐�������Ȃ��ꍇ�͌Ăяo���s��               
                if (!(index < actualParameterArray.length)) {
                    return false;
                }

                final ExpressionInfo actualParameter = actualParameterArray[index];
                TypeInfo actualType = actualParameter.getType();

                // <?> �� <? super A>�̏ꍇ��java.lang.Object�ɕϊ�����
                if (actualType instanceof ArbitraryTypeInfo || actualType instanceof SuperTypeInfo) {
                    final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                            .getClassInfo(new String[] { "java", "lang", "Object" });
                    actualType = new ClassTypeInfo(objectClass);
                }

                // <? extends B>�̏ꍇ�� B�ɕϊ�����
                else if (actualType instanceof ExtendsTypeInfo) {
                    actualType = ((ExtendsTypeInfo) actualType).getExtendsType();
                }

                if (!canCallWith(dummyType, actualType)) {
                    return false;
                }

                checkedActualIndex = index;
                continue;
            }
        }

        return (actualParameterArray.length - 1) == checkedActualIndex;
    }

    private static boolean canCallWith(final TypeInfo dummyType, final TypeInfo actualType) {

        //���������N���X�Q�ƌ^�̏ꍇ
        if (dummyType instanceof ClassTypeInfo) {

            final ClassInfo dummyClass = ((ClassTypeInfo) dummyType).getReferencedClass();

            // �������̌^��UnknownTypeInfo�̂Ƃ��͂ǂ����悤���Ȃ��̂�OK�ɂ���
            if (actualType instanceof UnknownTypeInfo) {
                return true;
            }

            // ��������Object�^�̂Ƃ��́C�N���X�Q�ƌ^�C�z��^�C�^�p�����[�^�^��OK
            final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                    .getClassInfo(new String[] { "java", "lang", "Object" });
            if (((ClassTypeInfo) dummyType).getReferencedClass().equals(objectClass)) {
                if (actualType instanceof ReferenceTypeInfo) {
                    return true;
                }
            }

            // AutoBoxing, InBoxing�̉\�����l��
            if (PrimitiveTypeInfo.isJavaWrapperType((ClassTypeInfo) dummyType)
                    && actualType instanceof PrimitiveTypeInfo) {
                if (PrimitiveTypeInfo.getPrimitiveType((ClassTypeInfo) dummyType) == actualType) {
                    return true;
                } else {
                    return false;
                }
            }

            if (!(actualType instanceof ClassTypeInfo)) {
                return false;
            }

            final ClassInfo actualClass = ((ClassTypeInfo) actualType).getReferencedClass();

            // �������C���������ɑΏۃN���X�ł���ꍇ�́C���̌p���֌W���l������D
            // �܂�C���������������̃T�u�N���X�łȂ��ꍇ�́C�Ăяo���\�ł͂Ȃ�
            if ((actualClass instanceof TargetClassInfo) && (dummyClass instanceof TargetClassInfo)) {

                // ���������������Ɠ����Q�ƌ^�i�N���X�j�ł��Ȃ��C�������̃T�u�N���X�ł��Ȃ��ꍇ�͊Y�����Ȃ�
                if (actualClass.equals(dummyClass)) {
                    return true;

                } else if (actualClass.isSubClass(dummyClass)) {
                    return true;

                } else {
                    return false;
                }
            }

            // �������C�������Ƃ��ɊO���N���X�ł���ꍇ�́C�����Ȃ��ŌĂяo���\�Ƃ���D
            // �������Ȃ��ƃ_���Ƃ��������͌��������Đ���������ł��Ȃ��ꍇ������D
            // �������C���������ɊO���N���X�ł���ꍇ�́C/*�������ꍇ�̂݌Ăяo��*/�\�Ƃ���
            else if ((actualClass instanceof ExternalClassInfo)
                    && (dummyClass instanceof ExternalClassInfo)) {
                return true;
            }

            // ���������O���N���X�C���������ΏۃN���X�̏ꍇ�́C�Ăяo���\�Ƃ���
            // �������Ȃ��ƃ_���Ƃ��������͌��������Đ���������ł��Ȃ��ꍇ������D
            else if ((actualClass instanceof TargetClassInfo)
                    && (dummyClass instanceof ExternalClassInfo)) {
                return true;
            }

            // ���������ΏۃN���X�C���������O���N���X�̏ꍇ�́C�Ăяo���\�Ƃ���
            // �������Ȃ��ƃ_���Ƃ��������͌��������Đ���������ł��Ȃ��ꍇ������D
            else {
                return true;
            }
        }

        // ���������v���~�e�B�u�^�̏ꍇ
        else if (dummyType instanceof PrimitiveTypeInfo) {

            // �������̌^��UnknownTypeInfo�̂Ƃ��͂ǂ����悤���Ȃ��̂�OK�ɂ���
            if (actualType instanceof UnknownTypeInfo) {
                return true;
            }

            // autoboxing, inboxing�̉\�����l��
            if (actualType instanceof ClassTypeInfo
                    && PrimitiveTypeInfo.isJavaWrapperType((ClassTypeInfo) actualType)) {
                if (PrimitiveTypeInfo.getPrimitiveType((ClassTypeInfo) actualType) == dummyType) {
                    return true;
                } else {
                    return false;
                }
            }

            // ���������v���~�e�B�u�^�łȂ��ꍇ�͌Ăяo���s��
            if (!(actualType instanceof PrimitiveTypeInfo)) {
                return false;
            }

            return true;
        }

        // ���������z��^�̏ꍇ
        else if (dummyType instanceof ArrayTypeInfo) {

            // �������̌^��UnknownTypeInfo�̂Ƃ��͂ǂ����悤���Ȃ��̂�OK�ɂ���
            if (actualType instanceof UnknownTypeInfo) {
                return true;
            }

            // ���������z��^�łȂ��ꍇ�͌Ăяo���s��
            if (!(actualType instanceof ArrayTypeInfo)) {
                return false;
            }

            // ���������Ⴄ�ꍇ�͌Ăяo���s��
            final int dummyDimenstion = ((ArrayTypeInfo) dummyType).getDimension();
            final int actualDimenstion = ((ArrayTypeInfo) actualType).getDimension();
            if (dummyDimenstion != actualDimenstion) {
                return false;
            }

            // �v�f�̌^���`�F�b�N
            final TypeInfo dummyElementType = ((ArrayTypeInfo) dummyType).getElementType();
            final TypeInfo actualElementType = ((ArrayTypeInfo) actualType).getElementType();
            return canCallWith(dummyElementType, actualElementType);
        }

        // ���������^�p�����[�^�^�̏ꍇ
        else if (dummyType instanceof TypeParameterTypeInfo) {

            // TODO ���̂Ƃ���C�����Ȃ���OK�ɂ��Ă���D�����̕K�v����
            return true;
        }

        assert false : "Here sholdn't be reached!";
        return true;
    }

    /**
     * ������ǉ�����
     * 
     * @param parameter �ǉ��������
     */
    public final void addParameter(final ParameterInfo parameter) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        this.parameters.add(parameter);
    }

    /**
     * ������ǉ�����
     * 
     * @param parameters �ǉ��������
     */
    public final void addParameters(final List<ParameterInfo> parameters) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameters) {
            throw new IllegalArgumentException();
        }

        this.parameters.addAll(parameters);
    }

    /**
     * ���̃��\�b�h�̈����� List ��Ԃ��D
     * 
     * @return ���̃��\�b�h�̈����� List
     */
    public final List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * ���̃��\�b�h�̈����̐���Ԃ�
     * 
     * @return ���̃��\�b�h�̈����̐�
     */
    public final int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * �����Ŏw�肳�ꂽ�^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^
     */
    @Override
    public final void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * �w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^��Ԃ�
     * 
     * @param index �^�p�����[�^�̃C���f�b�N�X
     * @return�@�w�肳�ꂽ�C���f�b�N�X�̌^�p�����[�^
     */
    @Override
    public final TypeParameterInfo getTypeParameter(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * �^�p�����[�^�� List ��Ԃ��D
     * 
     * @return ���̃N���X�̌^�p�����[�^�� List
     */
    @Override
    public final List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * �����ŗ^����ꂽ�^�p�����[�^�����̃��j�b�g�Œ�`���ꂽ���̂ł��邩��Ԃ�
     * 
     * @param typeParameter
     * @return
     */
    public final boolean isDefined(final TypeParameterInfo typeParameter) {

        final List<TypeParameterInfo> typeParameters = this.getTypeParameters();
        if (typeParameters.contains(typeParameter)) {
            return true;
        }

        return false;
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass;
    }

    /**
     * �����Ŏw�肳�ꂽ��O��ǉ�����
     * 
     * @param thrownException �ǉ������O
     */
    public final void addThrownException(final ReferenceTypeInfo thrownException) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == thrownException) {
            throw new IllegalArgumentException();
        }

        this.thrownExceptions.add(thrownException);
    }

    /**
     * �X���[������O�� List ��Ԃ��D
     * 
     * @return �X���[������O�� List
     */
    public final List<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableList(this.thrownExceptions);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo����ǉ�����D �v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param entityUsage ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo��
     */
    public void addUnresolvedUsage(final UnresolvedExpressionInfo<?> entityUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.unresolvedUsage.add(entityUsage);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set ��Ԃ��D
     * 
     * @return ���̃��\�b�h���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set
     */
    public Set<UnresolvedExpressionInfo<?>> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    @Override
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �q�N���X����Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �q�N���X����Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean isInheritanceVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isInheritanceVisible(this.modifiers);
    }

    /**
     * �������O��Ԃ���Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �������O��Ԃ���Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean isNamespaceVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isNamespaceVisible(this.modifiers);
    }

    /**
     * �ǂ�����ł��Q�Ɖ\���ǂ�����Ԃ�
     * 
     * @return �ǂ�����ł��Q�Ɖ\�ȏꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean isPublicVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isPublicVisible(this.modifiers);
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�܂��̓R���X�g���N�^��ǉ�����D�v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param caller �ǉ�����Ăяo�����\�b�h
     */
    public final void addCaller(final CallableUnitInfo caller) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caller) {
            return;
        }

        this.callers.add(caller);
    }

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�܂��̓R���X�g���N�^�� SortedSet ��Ԃ��D
     * 
     * @return ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�� SortedSet
     */
    public final SortedSet<CallableUnitInfo> getCallers() {
        return Collections.unmodifiableSortedSet(this.callers);
    }

    /**
     * �O���N���X�̃R���X�g���N�^�A���\�b�h�̈ʒu���ɓ����_�~�[�̒l�������� 
     */
    protected final static int getDummyPosition() {
        return dummyPosition--;
    }

    /**
     * ����CallableUnitInfo�̃V�O�l�`���̃e�L�X�g�\����Ԃ�
     * 
     * @return ����CallableUnitInfo�̃V�O�l�`���̃e�L�X�g�\��
     */
    public abstract String getSignatureText();

    /**
     * �C���q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * �^�p�����[�^��ۑ�����ϐ�
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * �X���[������O��ۑ�����ϐ�
     */
    private final List<ReferenceTypeInfo> thrownExceptions;

    /**
     * �����̃��X�g�̕ۑ����邽�߂̕ϐ�
     */
    private final List<ParameterInfo> parameters;

    /**
     * ���̃��\�b�h���Ăяo���Ă��郁�\�b�h�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<CallableUnitInfo> callers;

    /**
     * ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���Ȃǂ�ۑ����邽�߂̕ϐ�
     */
    private final transient Set<UnresolvedExpressionInfo<?>> unresolvedUsage;

    /**
     * �O���N���X�̃R���X�g���N�^�A���\�b�h�̈ʒu���ɓ����_�~�[�̒l�B
     */
    private static int dummyPosition = -1;
}
