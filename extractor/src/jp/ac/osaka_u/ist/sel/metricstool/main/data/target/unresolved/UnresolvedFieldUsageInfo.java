package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayLengthUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * �������t�B�[���h�g�p��ۑ����邽�߂̃N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedFieldUsageInfo extends UnresolvedVariableUsageInfo<FieldUsageInfo> {

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̌^���ƕϐ����C���p�\�Ȗ��O��Ԃ�^���ăI�u�W�F�N�g��������
     * 
     * @param memberImportStatements ���p�\�Ȗ��O���
     * @param qualifierUsage �t�B�[���h�g�p�����s�����e�G���e�B�e�B
     * @param fieldName �ϐ���
     * @param reference �t�B�[���h�g�p���Q�Ƃ�
     * @param assignment �t�B�[���h�g�p�������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedFieldUsageInfo(
            final List<UnresolvedMemberImportStatementInfo> memberImportStatements,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> qualifierUsage,
            final String fieldName, final boolean reference, final boolean assignment,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fieldName, reference, assignment, outerUnit, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == memberImportStatements) || (null == qualifierUsage) || (null == fieldName)) {
            throw new NullPointerException();
        }

        this.memberImportStatements = memberImportStatements;
        this.qualifierUsage = qualifierUsage;
        this.fieldName = fieldName;
    }

    /**
     * �������t�B�[���h�g�p���������C���̌^��Ԃ��D
     * 
     * @param usingClass �������t�B�[���h�g�p���s���Ă���N���X
     * @param usingMethod �������t�B�[���h�g�p���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς݃t�B�[���h�g�p
     */
    @Override
    public FieldUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �t�B�[���h���C�Q�ƁE������擾
        final String fieldName = this.getFieldName();
        final boolean reference = this.isReference();
        final boolean assignment = this.isAssignment();

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �t�B�[���h�g�p���������Ă���^("."�̑O�̂��)������
        final UnresolvedExpressionInfo<?> unresolvedQualifierUsage = this.getQualifierUsage();
        final ExpressionInfo qualifierUsage = unresolvedQualifierUsage.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert qualifierUsage != null : "resolveEntityUsage returned null!";

        final TypeInfo qualifierType = qualifierUsage.getType();
        this.resolvedInfo = this.resolve(usingClass, usingMethod, qualifierUsage, qualifierType,
                fieldName, reference, assignment, fromLine, fromColumn, toLine, toColumn,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert null != this.resolvedInfo : "resolvedInfo must not be null!";
        return this.resolvedInfo;
    }

    private FieldUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ExpressionInfo qualifierUsage,
            final TypeInfo qualifierType, final String fieldName, final boolean reference,
            final boolean assignment, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �^�p�����[�^�̏ꍇ�͂��̌p���^�����߂�
        if (qualifierType instanceof TypeParameterTypeInfo) {

            final TypeParameterInfo qualifierParameterType = ((TypeParameterTypeInfo) qualifierType)
                    .getReferncedTypeParameter();
            if (qualifierParameterType.hasExtendsType()) {
                for (final TypeInfo extendsType : qualifierParameterType.getExtendsTypes()) {
                    final FieldUsageInfo resolved = this.resolve(usingClass, usingMethod,
                            qualifierUsage, extendsType, fieldName, reference, assignment,
                            fromLine, fromColumn, toLine, toColumn, classInfoManager,
                            fieldInfoManager, methodInfoManager);
                    if (null != resolved) {
                        return resolved;
                    }
                }
            }

            else {
                final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                        .getClassInfo(new String[] { "java", "lang", "Object" });
                final FieldUsageInfo resolved = this.resolve(usingClass, usingMethod,
                        qualifierUsage, new ClassTypeInfo(objectClass), fieldName, reference,
                        assignment, fromLine, fromColumn, toLine, toColumn, classInfoManager,
                        fieldInfoManager, methodInfoManager);
                return resolved;

            }
        }

        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        else if (qualifierType instanceof UnknownTypeInfo) {

            final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);

            final FieldUsageInfo resolved = FieldUsageInfo.getInstance(qualifierUsage,
                    UnknownTypeInfo.getInstance(), unknownField, reference, assignment,
                    usingMethod, fromLine, fromColumn, toLine, toColumn);
            return resolved;

            //�e���N���X�^�̏ꍇ
        } else if (qualifierType instanceof ClassTypeInfo) {

            final ClassInfo ownerClass = ((ClassTypeInfo) qualifierType).getReferencedClass();
            // �e���ΏۃN���X(TargetClassInfo)�������ꍇ
            if (ownerClass instanceof TargetClassInfo) {

                // �܂��͗��p�\�ȃt�B�[���h���猟��
                {
                    // ���p�\�ȃt�B�[���h�ꗗ���擾
                    final List<FieldInfo> availableFields = NameResolver.getAvailableFields(
                            (TargetClassInfo) ownerClass, usingClass);

                    // ���p�\�ȃt�B�[���h���C�������t�B�[���h���Ō���
                    for (final FieldInfo availableField : availableFields) {

                        // ��v����t�B�[���h�������������ꍇ
                        if (fieldName.equals(availableField.getName())) {

                            final FieldUsageInfo resolved = FieldUsageInfo.getInstance(
                                    qualifierUsage, qualifierUsage.getType(), availableField,
                                    reference, assignment, usingMethod, fromLine, fromColumn,
                                    toLine, toColumn);
                            return resolved;
                        }
                    }
                }

                // �X�^�e�B�b�N�C���|�[�g����Ă���t�B�[���h��T��
                {
                    for (final UnresolvedMemberImportStatementInfo unresolvedMemberImportStatement : this
                            .getAvailableNamespaces()) {
                        final MemberImportStatementInfo memberImportStatement = unresolvedMemberImportStatement
                                .resolve(usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                        for (final Member importedMember : memberImportStatement.getImportedUnits()) {
                            if (importedMember instanceof FieldInfo) {
                                final FieldInfo importedField = (FieldInfo) importedMember;
                                if (fieldName.equals(importedField.getName())) {
                                    final ClassInfo classInfo = importedField.getOwnerClass();
                                    final ClassTypeInfo classType = new ClassTypeInfo(classInfo);
                                    final ClassReferenceInfo classReference = new ClassReferenceInfo(
                                            classType, usingMethod, fromLine, fromColumn, fromLine,
                                            fromColumn);
                                    final FieldUsageInfo resolved = FieldUsageInfo.getInstance(
                                            classReference, classType, importedField, reference,
                                            assignment, usingMethod, fromLine, fromColumn, toLine,
                                            toColumn);
                                    return resolved;
                                }
                            }
                        }
                    }
                }

                // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂�
                // ���̃N���X�̕ϐ����g�p���Ă���Ƃ݂Ȃ�
                {
                    for (ClassInfo classInfo = ownerClass; true; classInfo = ((InnerClassInfo) classInfo)
                            .getOuterClass()) {

                        final ExternalClassInfo externalSuperClass = NameResolver
                                .getExternalSuperClass(classInfo);
                        if (null != externalSuperClass) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                                    externalSuperClass);
                            fieldInfoManager.add(fieldInfo);

                            // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
                            final FieldUsageInfo resolved = FieldUsageInfo
                                    .getInstance(qualifierUsage, qualifierUsage.getType(),
                                            fieldInfo, reference, assignment, usingMethod,
                                            fromLine, fromColumn, toLine, toColumn);
                            return resolved;
                        }

                        if (!(classInfo instanceof TargetInnerClassInfo)) {
                            break;
                        }
                    }
                }

                // ������Ȃ������������s��
                {
                    err.println("Resolved as an external element, \"" + this.getFieldName() + "\""
                            + " line:" + this.getFromLine() + " column:" + this.getFromColumn()
                            + " on \"" + usingClass.getOwnerFile().getName());

                    final ExternalFieldInfo unknownField = new ExternalFieldInfo(fieldName);
                    final FieldUsageInfo resolved = FieldUsageInfo.getInstance(qualifierUsage,
                            UnknownTypeInfo.getInstance(), unknownField, reference, assignment,
                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                    return resolved;
                }

                // �e���O���N���X�iExternalClassInfo�j�������ꍇ
            } else if (ownerClass instanceof ExternalClassInfo) {

                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                        (ExternalClassInfo) ownerClass);
                fieldInfoManager.add(fieldInfo);

                // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
                final FieldUsageInfo resolved = FieldUsageInfo.getInstance(qualifierUsage,
                        qualifierUsage.getType(), fieldInfo, reference, assignment, usingMethod,
                        fromLine, fromColumn, toLine, toColumn);
                return resolved;
            }

        } else if (qualifierType instanceof ArrayTypeInfo) {

            // TODO �����͌���ˑ��ɂ��邵���Ȃ��̂��H �z��.length �Ȃ�

            // Java ����� �t�B�[���h���� length �������ꍇ�� int �^��Ԃ�
            // TODO�@�����Ƃ����Ȃ����Ȃ��Ƃ����Ȃ�
            final Settings settings = Settings.getInstance();
            if ((settings.getLanguage().equals(LANGUAGE.JAVA15)
                    || settings.getLanguage().equals(LANGUAGE.JAVA14) || settings.getLanguage()
                    .equals(LANGUAGE.JAVA13))
                    && fieldName.equals("length")) {

                final FieldUsageInfo resolved = new ArrayLengthUsageInfo(qualifierUsage,
                        (ArrayTypeInfo) qualifierType, usingMethod, fromLine, fromColumn, toLine,
                        toColumn);
                return resolved;
            }
        }

        return null;
    }

    /**
     * �g�p�\�Ȗ��O��Ԃ�Ԃ�
     * 
     * @return �g�p�\�Ȗ��O��Ԃ�Ԃ�
     */
    public List<UnresolvedMemberImportStatementInfo> getAvailableNamespaces() {
        return this.memberImportStatements;
    }

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����Ԃ�
     * 
     * @return �t�B�[���h�g�p�����s�����ϐ��̖������^��
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getQualifierUsage() {
        return this.qualifierUsage;
    }

    /**
     * �t�B�[���h����Ԃ�
     * 
     * @return �t�B�[���h��
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * �g�p�\�Ȗ��O��Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedMemberImportStatementInfo> memberImportStatements;

    /**
     * �t�B�[���h����ۑ����邽�߂̕ϐ�
     */
    private final String fieldName;

    /**
     * �t�B�[���h�g�p�����s�����ϐ��̖������^����ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> qualifierUsage;
}
