package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArbitraryTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExtendsTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SuperTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * ���������\�b�h�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodCallInfo extends UnresolvedCallInfo<MethodCallInfo> {

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^�C���\�b�h����^���ăI�u�W�F�N�g��������
     * 
     * @param memberImportStatements ���̃��\�b�h�Ăяo�����������̂��߂ɗ��p�ł���C���|�[�g��
     * @param qualifierUsage ���\�b�h�Ăяo�������s�����ϐ��̌^
     * @param methodName ���\�b�h��
     */
    public UnresolvedMethodCallInfo(
            final List<UnresolvedMemberImportStatementInfo> memberImportStatements,
            final UnresolvedExpressionInfo<?> qualifierUsage, final String methodName) {

        if ((null == memberImportStatements) && (null == qualifierUsage) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.memberImportStatements = memberImportStatements;
        this.qualifierUsage = qualifierUsage;
        this.methodName = methodName;
    }

    @Override
    public MethodCallInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
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

        // ���\�b�h�̃V�O�l�`�����擾
        final String name = this.getName();
        final List<ExpressionInfo> actualParameters = super.resolveArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final List<ReferenceTypeInfo> typeArguments = super.resolveTypeArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // ���\�b�h�Ăяo�����������Ă���^("."�̑O�̂��)������
        final UnresolvedExpressionInfo<?> unresolvedQualifierUsage = this.getQualifier();
        ExpressionInfo qualifierUsage = unresolvedQualifierUsage.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert qualifierUsage != null : "resolveEntityUsage returned null!";

        if (qualifierUsage instanceof UnknownEntityUsageInfo) {
            if (unresolvedQualifierUsage instanceof UnresolvedClassReferenceInfo) {

                final ExternalClassInfo externalClassInfo = UnresolvedClassReferenceInfo
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedQualifierUsage);
                classInfoManager.add(externalClassInfo);
                final ClassTypeInfo referenceType = new ClassTypeInfo(externalClassInfo);
                for (final UnresolvedTypeInfo<?> unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedQualifierUsage)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    referenceType.addTypeArgument(typeArgument);
                }
                qualifierUsage = new ClassReferenceInfo(referenceType, usingMethod, fromLine,
                        fromColumn, toLine, toColumn);
            }
        }

        final TypeInfo qualifierType = qualifierUsage.getType();
        this.resolvedInfo = this.resolve(usingClass, usingMethod, qualifierUsage, qualifierType,
                name, actualParameters, typeArguments, fromLine, fromColumn, toLine, toColumn,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert null != this.resolvedInfo : "resolvedInfo must not be null!";
        return this.resolvedInfo;
    }

    private MethodCallInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ExpressionInfo qualifierUsage,
            final TypeInfo qualifierType, final String methodName,
            final List<ExpressionInfo> actualParameters,
            final List<ReferenceTypeInfo> typeArguments, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �^�p�����[�^�̏ꍇ�͂��̌p���^�����߂�
        if (qualifierType instanceof TypeParameterTypeInfo) {

            final TypeParameterInfo qualifierParameterType = ((TypeParameterTypeInfo) qualifierType)
                    .getReferncedTypeParameter();

            // extends ������ꍇ
            if (qualifierParameterType.hasExtendsType()) {
                for (final TypeInfo extendsType : qualifierParameterType.getExtendsTypes()) {
                    final MethodCallInfo resolve = this.resolve(usingClass, usingMethod,
                            qualifierUsage, extendsType, methodName, actualParameters,
                            typeArguments, fromLine, fromColumn, toLine, toColumn,
                            classInfoManager, fieldInfoManager, methodInfoManager);
                    if (null != resolve) {
                        return resolve;
                    }
                }
            }

            // extends ���Ȃ��ꍇ
            else {
                final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                        .getClassInfo(new String[] { "java", "lang", "Object" });
                final MethodCallInfo resolve = this.resolve(usingClass, usingMethod,
                        qualifierUsage, new ClassTypeInfo(objectClass), methodName,
                        actualParameters, typeArguments, fromLine, fromColumn, toLine, toColumn,
                        classInfoManager, fieldInfoManager, methodInfoManager);
                return resolve;
            }
        }

        // <?>��<? super A>�̃J�b�R���̌^�̎�
        else if (qualifierType instanceof ArbitraryTypeInfo
                || qualifierType instanceof SuperTypeInfo) {

            final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                    .getClassInfo(new String[] { "java", "lang", "Object" });
            final MethodCallInfo resolve = this.resolve(usingClass, usingMethod, qualifierUsage,
                    new ClassTypeInfo(objectClass), methodName, actualParameters, typeArguments,
                    fromLine, fromColumn, toLine, toColumn, classInfoManager, fieldInfoManager,
                    methodInfoManager);
            return resolve;
        }

        // <? extends B> �̃J�b�R���̌^�̎�
        else if (qualifierType instanceof ExtendsTypeInfo) {

            final TypeInfo extendsType = ((ExtendsTypeInfo) qualifierType).getExtendsType();
            final MethodCallInfo resolve = this.resolve(usingClass, usingMethod, qualifierUsage,
                    extendsType, methodName, actualParameters, typeArguments, fromLine, fromColumn,
                    toLine, toColumn, classInfoManager, fieldInfoManager, methodInfoManager);
            return resolve;
        }

        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        else if (qualifierType instanceof UnknownTypeInfo) {

            final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(methodName);
            final MethodCallInfo resolved = new MethodCallInfo(qualifierType, qualifierUsage,
                    unknownMethod, UnknownTypeInfo.getInstance(), usingMethod, fromLine,
                    fromColumn, toLine, toColumn);
            resolved.addArguments(actualParameters);
            resolved.addTypeArguments(typeArguments);
            return resolved;

            // �e���N���X�^�������ꍇ
        } else if (qualifierType instanceof ClassTypeInfo
                || qualifierType instanceof PrimitiveTypeInfo) {

            final ClassInfo ownerClass;
            if (qualifierType instanceof PrimitiveTypeInfo) {
                final Settings settings = Settings.getInstance();
                ownerClass = TypeConverter.getTypeConverter(settings.getLanguage())
                        .getWrapperClass((PrimitiveTypeInfo) qualifierType);
            } else {
                ownerClass = ((ClassTypeInfo) qualifierType).getReferencedClass();
            }

            // �܂��͗��p�\�ȃ��\�b�h���猟��
            {
                // ���p�\�ȃ��\�b�h�ꗗ���擾
                final List<MethodInfo> availableMethods = NameResolver.getAvailableMethods(
                        ownerClass, usingClass);

                // ���p�\�ȃ��\�b�h����C���������\�b�h�ƈ�v������̂�����
                // ���\�b�h���C�����̌^�̃��X�g��p���āC���̃��\�b�h�̌Ăяo���ł��邩�ǂ����𔻒�
                for (final MethodInfo availableMethod : availableMethods) {

                    // �Ăяo���\�ȃ��\�b�h�����������ꍇ
                    if (availableMethod.canCalledWith(methodName, actualParameters)) {

                        final TypeInfo returnType = availableMethod.getReturnType();

                        // �Ԃ�l���^�p�����[�^�̏ꍇ
                        if (returnType instanceof TypeParameterTypeInfo) {
                            final TypeParameterInfo referencedTypeParameter = ((TypeParameterTypeInfo) returnType)
                                    .getReferncedTypeParameter();
                            final TypeInfo typeArgument;

                            // ���\�b�h�̌^�p�����[�^���猟��
                            if (availableMethod.isDefined(referencedTypeParameter)) {
                                final int index = referencedTypeParameter.getIndex();
                                if (index < typeArguments.size()) {
                                    typeArgument = typeArguments.get(index);
                                } else {
                                    final ClassInfo objectClass = classInfoManager
                                            .getClassInfo(new String[] { "java", "lang", "Object" });
                                    typeArgument = new ClassTypeInfo(objectClass);
                                }
                            }

                            // �N���X�̌^�p�����[�^���猟��
                            else if (((ClassTypeInfo) qualifierType).getReferencedClass()
                                    .isDefined(referencedTypeParameter)) {
                                typeArgument = ((ClassTypeInfo) qualifierType)
                                        .getTypeArgument(referencedTypeParameter);
                            }

                            // �{���͂����̓G���[���o���ׂ�
                            else {
                                final ClassInfo objectClass = classInfoManager
                                        .getClassInfo(new String[] { "java", "lang", "Object" });
                                typeArgument = new ClassTypeInfo(objectClass);
                            }

                            final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                                    qualifierUsage, availableMethod, typeArgument, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            resolved.addArguments(actualParameters);
                            resolved.addTypeArguments(typeArguments);
                            return resolved;
                        }

                        // �Ԃ�l���^�p�����[�^�łȂ��ꍇ
                        else {
                            final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                                    qualifierUsage, availableMethod, returnType, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            resolved.addArguments(actualParameters);
                            resolved.addTypeArguments(typeArguments);
                            return resolved;
                        }
                    }
                }
            }

            // �X�^�e�B�b�N�C���|�[�g����Ă��郁�\�b�h��T��
            {
                for (final UnresolvedMemberImportStatementInfo unresolvedMemberImportStatement : this
                        .getImportStatements()) {
                    final MemberImportStatementInfo memberImportStatement = unresolvedMemberImportStatement
                            .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                    for (final Member importedMember : memberImportStatement.getImportedUnits()) {
                        if (importedMember instanceof MethodInfo) {
                            final MethodInfo importedMethod = (MethodInfo) importedMember;

                            // �Ăяo���\�ȃ��\�b�h�����������ꍇ
                            if (importedMethod.canCalledWith(methodName, actualParameters)) {

                                final TypeInfo returnType = importedMethod.getReturnType();

                                // �Ԃ�l���^�p�����[�^�̏ꍇ
                                if (returnType instanceof TypeParameterTypeInfo) {
                                    final TypeParameterInfo referencedTypeParameter = ((TypeParameterTypeInfo) returnType)
                                            .getReferncedTypeParameter();
                                    final TypeInfo typeArgument;
                                    if (importedMethod.isDefined(referencedTypeParameter)) {
                                        final int index = referencedTypeParameter.getIndex();
                                        if (index < typeArguments.size()) {
                                            typeArgument = typeArguments.get(index);
                                        } else {
                                            final ClassInfo objectClass = classInfoManager
                                                    .getClassInfo(new String[] { "java", "lang",
                                                            "Object" });
                                            typeArgument = new ClassTypeInfo(objectClass);
                                        }
                                    }

                                    // �N���X�̌^�p�����[�^���猟��
                                    else if (((ClassTypeInfo) qualifierType).getReferencedClass()
                                            .isDefined(referencedTypeParameter)) {
                                        typeArgument = ((ClassTypeInfo) qualifierType)
                                                .getTypeArgument(referencedTypeParameter);
                                    }

                                    // �{���͂����̓G���[���o���ׂ�
                                    else {
                                        final ClassInfo objectClass = classInfoManager
                                                .getClassInfo(new String[] { "java", "lang",
                                                        "Object" });
                                        typeArgument = new ClassTypeInfo(objectClass);
                                    }

                                    final MethodCallInfo resolved = new MethodCallInfo(
                                            qualifierType, qualifierUsage, importedMethod,
                                            typeArgument, usingMethod, fromLine, fromColumn,
                                            toLine, toColumn);
                                    resolved.addArguments(actualParameters);
                                    resolved.addTypeArguments(typeArguments);
                                    return resolved;
                                }

                                // �Ԃ�l���^�p�����[�^�łȂ��ꍇ
                                else {
                                    final MethodCallInfo resolved = new MethodCallInfo(
                                            qualifierType, qualifierUsage, importedMethod,
                                            returnType, usingMethod, fromLine, fromColumn, toLine,
                                            toColumn);
                                    resolved.addArguments(actualParameters);
                                    resolved.addTypeArguments(typeArguments);
                                    return resolved;
                                }
                            }
                        }
                    }
                }
            }

            // ���p�\�ȃ��\�b�h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
            // ���̃N���X�̃��\�b�h���g�p���Ă���Ƃ݂Ȃ�
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass(ownerClass);
                if (null != externalSuperClass) {

                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName());
                    methodInfo.setOuterUnit(externalSuperClass);
                    final List<ParameterInfo> dummyParameters = ExternalParameterInfo
                            .createParameters(actualParameters, methodInfo);
                    methodInfo.addParameters(dummyParameters);
                    methodInfoManager.add(methodInfo);

                    // �O���N���X�ɐV�K�ŊO�����\�b�h�ϐ��iExternalMethodInfo�j��ǉ������̂Ō^�͕s��
                    final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                            qualifierUsage, methodInfo, UnknownTypeInfo.getInstance(), usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    resolved.addArguments(actualParameters);
                    resolved.addTypeArguments(typeArguments);
                    return resolved;
                }
            }

            // �e���O���N���X�iExternalClassInfo�j�������ꍇ
            if (ownerClass instanceof ExternalClassInfo) {

                err.println("Resolved as an external element, \"" + this.getName() + "\""
                        + " line:" + this.getFromLine() + " column:" + this.getFromColumn()
                        + " on \"" + usingClass.getOwnerFile().getName() + "\"");

                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName());
                methodInfo.setOuterUnit(ownerClass);
                final List<ParameterInfo> parameters = ExternalParameterInfo.createParameters(
                        actualParameters, methodInfo);
                methodInfo.addParameters(parameters);
                methodInfoManager.add(methodInfo);

                // �O���N���X�ɐV�K�ŊO�����\�b�h(ExternalMethodInfo)��ǉ������̂Ō^�͕s���D
                final MethodCallInfo resolved = new MethodCallInfo(qualifierType, qualifierUsage,
                        methodInfo, UnknownTypeInfo.getInstance(), usingMethod, fromLine,
                        fromColumn, toLine, toColumn);
                resolved.addArguments(actualParameters);
                resolved.addTypeArguments(typeArguments);
                return resolved;
            }

            // �e���z�񂾂����ꍇ
        } else if (qualifierType instanceof ArrayTypeInfo) {

            // XXX Java����ł���΁C java.lang.Object �ɑ΂���Ăяo��
            final Settings settings = Settings.getInstance();
            if (settings.getLanguage().equals(LANGUAGE.JAVA15)
                    || settings.getLanguage().equals(LANGUAGE.JAVA14)
                    || settings.getLanguage().equals(LANGUAGE.JAVA13)) {
                final ClassInfo ownerClass = classInfoManager.getClassInfo(new String[] { "java",
                        "lang", "Object" });

                if (ownerClass instanceof ExternalClassInfo) {
                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(this.getName());
                    methodInfo.setOuterUnit(ownerClass);
                    final List<ParameterInfo> parameters = ExternalParameterInfo.createParameters(
                            actualParameters, methodInfo);
                    methodInfo.addParameters(parameters);
                    methodInfoManager.add(methodInfo);

                    // �O���N���X�ɐV�K�ŊO�����\�b�h��ǉ������̂Ō^�͕s��
                    final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                            qualifierUsage, methodInfo, UnknownTypeInfo.getInstance(), usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    resolved.addArguments(actualParameters);
                    resolved.addTypeArguments(typeArguments);
                    return resolved;
                }

                else if (ownerClass instanceof TargetClassInfo) {

                    // ���p�\�ȃ��\�b�h�ꗗ���擾, NameResolver.getAvailableMethod�͂����Ă͂��߁D
                    //�@�Ȃ��Ȃ�C���̃R���e�L�X�g�ł͉����C���q�Ɋ֌W�Ȃ��C���ׂẴ��\�b�h�����p�\
                    final List<MethodInfo> availableMethods = new LinkedList<MethodInfo>();
                    availableMethods.addAll(((TargetClassInfo) ownerClass).getDefinedMethods());

                    // ���p�\�ȃ��\�b�h����C���������\�b�h�ƈ�v������̂�����
                    // ���\�b�h���C�����̌^�̃��X�g��p���āC���̃��\�b�h�̌Ăяo���ł��邩�ǂ����𔻒�
                    for (final MethodInfo availableMethod : availableMethods) {

                        // �Ăяo���\�ȃ��\�b�h�����������ꍇ
                        if (availableMethod.canCalledWith(methodName, actualParameters)) {
                            final TypeInfo returnType = availableMethod.getReturnType();

                            // �Ԃ�l���^�p�����[�^�̏ꍇ
                            if (returnType instanceof TypeParameterTypeInfo) {
                                final TypeParameterInfo referencedTypeParameter = ((TypeParameterTypeInfo) returnType)
                                        .getReferncedTypeParameter();

                                final TypeInfo typeArgument;

                                // ���\�b�h�̌^�p�����[�^���猟��
                                if (availableMethod.isDefined(referencedTypeParameter)) {
                                    final int index = referencedTypeParameter.getIndex();
                                    if (index < typeArguments.size()) {
                                        typeArgument = typeArguments.get(index);
                                    } else {
                                        final ClassInfo objectClass = classInfoManager
                                                .getClassInfo(new String[] { "java", "lang",
                                                        "Object" });
                                        typeArgument = new ClassTypeInfo(objectClass);
                                    }
                                }

                                // �N���X�̌^�p�����[�^���猟��
                                else if (((ClassTypeInfo) qualifierType).getReferencedClass()
                                        .isDefined(referencedTypeParameter)) {
                                    typeArgument = ((ClassTypeInfo) qualifierType)
                                            .getTypeArgument(referencedTypeParameter);
                                }

                                // �{���͂����̓G���[���o���ׂ�
                                else {
                                    final ClassInfo objectClass = classInfoManager
                                            .getClassInfo(new String[] { "java", "lang", "Object" });
                                    typeArgument = new ClassTypeInfo(objectClass);
                                }

                                final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                                        qualifierUsage, availableMethod, typeArgument, usingMethod,
                                        fromLine, fromColumn, toLine, toColumn);
                                resolved.addArguments(actualParameters);
                                resolved.addTypeArguments(typeArguments);
                                return resolved;
                            }

                            // �Ԃ�l���^�p�����[�^�łȂ��ꍇ
                            else {
                                final MethodCallInfo resolved = new MethodCallInfo(qualifierType,
                                        qualifierUsage, availableMethod, returnType, usingMethod,
                                        fromLine, fromColumn, toLine, toColumn);
                                resolved.addArguments(actualParameters);
                                resolved.addTypeArguments(typeArguments);
                                return resolved;
                            }
                        }
                    }
                }
            }
        }

        assert false : "Here should not be reached!";
        final ExternalMethodInfo unknownMethod = new ExternalMethodInfo(methodName);
        final MethodCallInfo resolved = new MethodCallInfo(qualifierType, qualifierUsage,
                unknownMethod, UnknownTypeInfo.getInstance(), usingMethod, fromLine, fromColumn,
                toLine, toColumn);
        resolved.addArguments(actualParameters);
        resolved.addTypeArguments(typeArguments);
        return resolved;
    }

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̌^��Ԃ�
     * 
     * @return ���\�b�h�Ăяo�������s�����ϐ��̌^
     */
    public UnresolvedExpressionInfo<?> getQualifier() {
        return this.qualifierUsage;
    }

    /**
     * ���\�b�h����Ԃ�
     * 
     * @return ���\�b�h��
     */
    public final String getName() {
        return this.methodName;
    }

    /**
     * ���\�b�h����ۑ����邽�߂̕ϐ�
     */
    protected String methodName;

    public List<UnresolvedMemberImportStatementInfo> getImportStatements() {
        return this.memberImportStatements;
    }

    /**
     * ���\�b�h�Ăяo�������s�����ϐ��̎Q�Ƃ�ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedExpressionInfo<?> qualifierUsage;

    private final List<UnresolvedMemberImportStatementInfo> memberImportStatements;
}
