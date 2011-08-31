package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedCaseLabelInfo extends UnresolvedExpressionInfo<ExpressionInfo> {

    public UnresolvedCaseLabelInfo(final UnresolvedExpressionInfo<?> label) {
        this.label = label;
        this.ownerCaseEntry = null;
        this.setFromLine(label.getFromLine());
        this.setFromColumn(label.getFromColumn());
        this.setToLine(label.getToLine());
        this.setToColumn(label.getToColumn());
    }

    public UnresolvedExpressionInfo<?> getLabel() {
        return this.label;
    }

    public UnresolvedCaseEntryInfo getOwnerCaseEntry() {
        return this.ownerCaseEntry;
    }

    public void setOwnerCaseEntry(final UnresolvedCaseEntryInfo ownerCaseEntry) {

        if (null == ownerCaseEntry) {
            throw new IllegalArgumentException();
        }

        this.ownerCaseEntry = ownerCaseEntry;
    }

    @Override
    public ExpressionInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        if (null == this.getOwnerCaseEntry()) {
            throw new IllegalStateException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // ���̃��x���I�u�W�F�N�g�̈ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // switch���̎��̌^�𒲂ׂ� 
        final UnresolvedCaseEntryInfo unresolvedOwnerCaseEntry = this.getOwnerCaseEntry();
        final UnresolvedSwitchBlockInfo unresolvedOwnerSwitchBlock = unresolvedOwnerCaseEntry
                .getOwnerSwitchBlock();
        final UnresolvedExpressionInfo<?> unresolvedExpression = (UnresolvedExpressionInfo<?>) unresolvedOwnerSwitchBlock
                .getConditionalClause().getCondition();
        final ExpressionInfo expression = unresolvedExpression.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final TypeInfo type = expression.getType();

        final UnresolvedExpressionInfo<?> unresolvedLabel = this.getLabel();

        // �v���~�e�B�u�^�����̃��b�p�[�N���X�̂Ƃ�
        if ((type instanceof PrimitiveTypeInfo) || PrimitiveTypeInfo.isJavaWrapperType(type)) {

            this.resolvedInfo = unresolvedLabel.resolve(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
            return this.resolvedInfo;
        }

        //�@�^���s���ȂƂ�
        else if (type instanceof UnknownTypeInfo) {

            // TODO �Ƃ肠�����v���~�e�B�u�^�Ɠ����悤�ɉ�͂���D��肠��̉\���D
            this.resolvedInfo = unresolvedLabel.resolve(usingClass, usingMethod, classInfoManager,
                    fieldInfoManager, methodInfoManager);
            return this.resolvedInfo;
        }

        //�@����ȊO�̂Ƃ�
        else {

            if (!(unresolvedLabel instanceof UnresolvedVariableUsageInfo<?>)) {
                throw new IllegalStateException();
            }

            if (!(type instanceof ClassTypeInfo)) {
                throw new IllegalStateException();
            }

            final String name = ((UnresolvedVariableUsageInfo<?>) unresolvedLabel).getUsedVariableName();

            final CallableUnitInfo ownerMethod = expression.getOwnerMethod();

            final ClassInfo referencedClass = ((ClassTypeInfo) type).getReferencedClass();
            // TODO �{����enum�ŗ񋓂���Ă�����̂̓T�u�N���X�Ƃ��ĉ�͂���Ă���ׂ�
            //for (final ClassInfo subClass : referencedClass.getSubClasses()) {
            //    if (subClass.getClassName().equals(name)) {
            //        this.resolvedInfo = new CaseLabelInfo(new ClassReferenceInfo(new ClassTypeInfo(
            //                subClass), ownerMethod, fromLine, fromColumn, toLine, toColumn),
            //                fromLine, fromColumn, toLine, toColumn);
            //        return this.resolvedInfo; 
            //    }
            //}
            for (final FieldInfo field : referencedClass.getDefinedFields()) {
                if (field.getName().equals(name)) {

                    final String[] referencedClassFQName = referencedClass.getFullQualifiedName();
                    final String[] fqName = new String[referencedClassFQName.length + 1];
                    System.arraycopy(referencedClassFQName, 0, fqName, 0,
                            referencedClassFQName.length);
                    fqName[fqName.length - 1] = name;
                    ClassInfo innerClass = classInfoManager.getClassInfo(fqName);
                    if (null == innerClass) {
                        innerClass = new ExternalInnerClassInfo(fqName, referencedClass);
                        classInfoManager.add(innerClass);
                    }
                    this.resolvedInfo = new ClassReferenceInfo(new ClassTypeInfo(innerClass),
                            ownerMethod, fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }
            }

            // �O���N���X�̏ꍇ�́C�T�u�N���X��������̂Ƃ���
            if (referencedClass instanceof ExternalClassInfo) {
                final String[] referencedClassFQName = referencedClass.getFullQualifiedName();
                final String[] fqName = new String[referencedClassFQName.length + 1];
                System.arraycopy(referencedClassFQName, 0, fqName, 0, referencedClassFQName.length);
                referencedClassFQName[referencedClassFQName.length - 1] = name;
                ClassInfo innerClass = classInfoManager.getClassInfo(fqName);
                if (null == innerClass) {
                    innerClass = new ExternalInnerClassInfo(fqName, referencedClass);
                    classInfoManager.add(innerClass);
                }
                this.resolvedInfo = new ClassReferenceInfo(new ClassTypeInfo(innerClass),
                        ownerMethod, fromLine, fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }

            assert false : "Here shouldn't be reached.";
        }

        return null;
    }

    final private UnresolvedExpressionInfo<?> label;

    private UnresolvedCaseEntryInfo ownerCaseEntry;
}
