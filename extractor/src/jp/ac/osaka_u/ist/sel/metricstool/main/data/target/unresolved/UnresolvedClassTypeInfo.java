package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������N���X�^��\���N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedClassTypeInfo implements UnresolvedReferenceTypeInfo<ReferenceTypeInfo> {

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     */
    public UnresolvedClassTypeInfo(
            final List<UnresolvedClassImportStatementInfo> availableNamespaces,
            final String[] referenceName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.referenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.typeArguments = new LinkedList<UnresolvedTypeInfo<? extends TypeInfo>>();
    }

    /**
     * ���̖������N���X�^�����łɉ����ς݂��ǂ�����Ԃ��D
     * 
     * @return �����ς݂̏ꍇ�� true�C��������Ă��Ȃ��ꍇ�� false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���̖������N���X�^�̉����ς݂̌^��Ԃ�
     */
    @Override
    public ReferenceTypeInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public ReferenceTypeInfo resolve(final TargetClassInfo usingClass,
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

        // import ���Ŏw�肳��Ă���N���X���o�^����Ă��Ȃ��Ȃ�C�O���N���X�Ƃ��ēo�^����
        for (final UnresolvedClassImportStatementInfo availableNamespace : this
                .getAvailableNamespaces()) {

            if (!availableNamespace.isAll()) {
                final String[] fullQualifiedName = availableNamespace.getImportName();
                if (!classInfoManager.hasClassInfo(fullQualifiedName)) {
                    final ExternalClassInfo externalClassInfo = new ExternalClassInfo(
                            fullQualifiedName);
                    classInfoManager.add(externalClassInfo);
                }
            }
        }

        // �o�^����Ă���N���X�����猟�o
        final String[] referenceName = this.getReferenceName();
        final Collection<ClassInfo> candidateClasses = classInfoManager
                .getClassInfos(referenceName[referenceName.length - 1]);

        //�������Q�Ƃ̏ꍇ�͊��S���薼���ǂ����𒲂ׂ�C�P���Q�Ƃ̏ꍇ�̓f�t�H���g�p�b�P�[�W���璲�ׂ�
        {
            final ClassInfo matchedClass = classInfoManager.getClassInfo(referenceName);
            if (null != matchedClass) {
                final ClassTypeInfo classType = new ClassTypeInfo(matchedClass);
                for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    classType.addTypeArgument(typeArgument);
                }
                this.resolvedInfo = classType;
                return this.resolvedInfo;
            }
        }

        // �C���|�[�g����Ă���N���X���猟���i�P���Q�Ƃ̏ꍇ�j
        // �������C�����Ɠ����p�b�P�[�W�̃N���X�͏��O����
        if (this.isMoniminalReference()) {
            for (final UnresolvedClassImportStatementInfo unresolvedClassImportStatement : this
                    .getAvailableNamespaces()) {

                final ClassImportStatementInfo classImportStatement = unresolvedClassImportStatement
                        .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                methodInfoManager);

                // �����Ɠ����p�b�P�[�W�͏��O����
                {
                    final TargetClassInfo outestUsingClass = (usingClass instanceof TargetInnerClassInfo) ? (TargetClassInfo) TargetInnerClassInfo
                            .getOutestClass((TargetInnerClassInfo) usingClass)
                            : usingClass;
                    if (classImportStatement.getNamespace().equals(outestUsingClass.getNamespace())) {
                        continue;
                    }
                }

                for (final ClassInfo importedClass : classImportStatement.getImportedUnits()) {
                    if (candidateClasses.contains(importedClass)) {
                        final ClassTypeInfo classType = new ClassTypeInfo(importedClass);
                        for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            classType.addTypeArgument(typeArgument);
                        }
                        this.resolvedInfo = classType;
                        return this.resolvedInfo;
                    }
                }
            }
        }

        // �P���Q�Ƃ̏ꍇ�̓C���|�[�g����p���Ȃ��ł����p�\�ȃN���X���猟��
        // �C���|�[�g����Ă���N���X����̌����������ɂȂ��Ƃ����Ȃ�
        if (this.isMoniminalReference()) {

            for (final ClassInfo availableClass : NameResolver.getAvailableClasses(usingClass)) {
                if (candidateClasses.contains(availableClass)) {
                    final ClassTypeInfo classType = new ClassTypeInfo(availableClass);
                    for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        classType.addTypeArgument(typeArgument);
                    }
                    this.resolvedInfo = classType;
                    return this.resolvedInfo;
                }
            }
        }

        // �P�s�Q�Ƃ̏ꍇ�́C�����̃p�b�P�[�W�̃N���X�����p�\�Ȃ̂ŁC����
        if (this.isMoniminalReference()) {
            for (final UnresolvedClassImportStatementInfo unresolvedClassImportStatement : this
                    .getAvailableNamespaces()) {

                final ClassImportStatementInfo classImportStatement = unresolvedClassImportStatement
                        .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                methodInfoManager);

                // �����Ɠ����p�b�P�[�W�ȊO�͏��O����
                {
                    final TargetClassInfo outestUsingClass = (usingClass instanceof TargetInnerClassInfo) ? (TargetClassInfo) TargetInnerClassInfo
                            .getOutestClass((TargetInnerClassInfo) usingClass)
                            : usingClass;
                    if (!classImportStatement.getNamespace()
                            .equals(outestUsingClass.getNamespace())) {
                        continue;
                    }
                }

                for (final ClassInfo importedClass : classImportStatement.getImportedUnits()) {
                    if (candidateClasses.contains(importedClass)) {
                        final ClassTypeInfo classType = new ClassTypeInfo(importedClass);
                        for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            classType.addTypeArgument(typeArgument);
                        }
                        this.resolvedInfo = classType;
                        return this.resolvedInfo;
                    }
                }
            }
        }

        // �C���|�[�g����Ă���N���X���猟���i�������Q�Ƃ̏ꍇ�j 
        if (!this.isMoniminalReference()) {

            for (final UnresolvedClassImportStatementInfo unresolvedClassImportStatement : this
                    .getAvailableNamespaces()) {

                final ClassImportStatementInfo classImportStatement = unresolvedClassImportStatement
                        .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                methodInfoManager);

                for (final ClassInfo importedClass : classImportStatement.getImportedUnits()) {

                    for (final ClassInfo candidateClass : candidateClasses) {

                        final String[] candidateFQName = candidateClass.getFullQualifiedName();

                        CLASS: for (final ClassInfo accessibleInnerClass : TargetClassInfo
                                .getAccessibleInnerClasses(importedClass)) {

                            final String[] availableFQName = accessibleInnerClass
                                    .getFullQualifiedName();

                            for (int index = 1; index <= referenceName.length; index++) {
                                if (!availableFQName[availableFQName.length - index]
                                        .equals(referenceName[referenceName.length - index])) {
                                    continue CLASS;
                                }
                            }

                            for (int index = 1; index <= referenceName.length; index++) {
                                if (!candidateFQName[candidateFQName.length - index]
                                        .equals(referenceName[referenceName.length - index])) {
                                    continue CLASS;
                                }
                            }

                            final ClassTypeInfo classType = new ClassTypeInfo(candidateClass);
                            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                                    .getTypeArguments()) {
                                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                        usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                                classType.addTypeArgument(typeArgument);
                            }
                            this.resolvedInfo = classType;
                            return this.resolvedInfo;
                        }
                    }
                }
            }
        }

        // �P���Q�Ƃ̏ꍇ�͌^�p�����[�^���ǂ����𒲂ׂ�
        if (this.isMoniminalReference()) {

            TypeParameterizable typeParameterizableUnit = null != usingMethod ? usingMethod
                    : usingClass;
            do {
                for (final TypeParameterInfo typeParameter : typeParameterizableUnit
                        .getTypeParameters()) {
                    if (typeParameter.getName().equals(referenceName[0])) {
                        this.resolvedInfo = new TypeParameterTypeInfo(typeParameter);
                        return this.resolvedInfo;
                    }
                }
                typeParameterizableUnit = typeParameterizableUnit.getOuterTypeParameterizableUnit();
            } while (null != typeParameterizableUnit);
        }

        //�����ɂ���̂́C�N���X��������Ȃ������Ƃ�
        if (this.isMoniminalReference()) {

            //System.out.println(referenceName[0]);
            final ExternalClassInfo externalClassInfo = new ExternalClassInfo(referenceName[0]);
            final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                classType.addTypeArgument(typeArgument);
            }
            this.resolvedInfo = classType;

        } else {

            // �C���|�[�g�����Q�Ɩ���g�ݍ��킹�邱�Ƃ��ł��邩������
            // ���Ƃ��΁C import A.B.C �ŁC�Q�Ɩ����CC.D�ł���΁C���S���薼��A.B.C.D�̃N���X�����邱�ƂɂȂ�
            for (final UnresolvedClassImportStatementInfo availableNamespace : this
                    .getAvailableNamespaces()) {

                if (!availableNamespace.isAll()) {
                    final String[] importedName = availableNamespace.getFullQualifiedName();
                    if (importedName[importedName.length - 1].equals(referenceName[0])) {
                        final String[] fqName = new String[referenceName.length
                                + importedName.length - 1];
                        int index = 0;
                        for (; index < importedName.length; index++) {
                            fqName[index] = importedName[index];
                        }
                        for (int i = 1; i < referenceName.length; i++, index++) {
                            fqName[index] = referenceName[i];
                        }

                        final ExternalClassInfo externalClassInfo = new ExternalClassInfo(fqName);
                        final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
                        for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            classType.addTypeArgument(typeArgument);
                        }
                        this.resolvedInfo = classType;
                        return this.resolvedInfo;
                    }
                }

            }

            final ExternalClassInfo externalClassInfo = referenceName.length > 2 ? new ExternalClassInfo(
                    referenceName)
                    : new ExternalClassInfo(referenceName[0]);
            final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                classType.addTypeArgument(typeArgument);
            }
            this.resolvedInfo = classType;
        }

        return this.resolvedInfo;
    }

    public ReferenceTypeInfo resolveAsSuperType(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        return this.resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);
        //        // �s���ȌĂяo���łȂ������`�F�b�N
        //        MetricsToolSecurityManager.getInstance().checkAccess();
        //        if ((null == usingClass) || (null == classInfoManager)) {
        //            throw new IllegalArgumentException();
        //        }
        //
        //        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        //        if (this.alreadyResolved()) {
        //            return this.getResolved();
        //        }
        //
        //        final String[] referenceName = this.getReferenceName();
        //        final Collection<ClassInfo> candidates = classInfoManager
        //                .getClassInfosWithSuffix(referenceName);
        //
        //        if (candidates.isEmpty()) {
        //
        //            final ExternalClassInfo superClass = 1 == referenceName.length ? new ExternalClassInfo(
        //                    referenceName[0]) : new ExternalClassInfo(referenceName);
        //            classInfoManager.add(superClass);
        //            final ClassTypeInfo superClassType = new ClassTypeInfo(superClass);
        //            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
        //                    .getTypeArguments()) {
        //                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
        //                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        //                superClassType.addTypeArgument(typeArgument);
        //            }
        //            this.resolvedInfo = superClassType;
        //            return this.resolvedInfo;
        //        }
        //
        //        else {
        //            int longestMatchedLength = -1;
        //            ClassInfo bestCandidate = null;
        //            for (final ClassInfo candidate : candidates) {
        //
        //                final int matchedLength = this.getMatchedLength(usingClass.getFullQualifiedName(),
        //                        candidate.getFullQualifiedName());
        //                if (longestMatchedLength < matchedLength) {
        //                    longestMatchedLength = matchedLength;
        //                    bestCandidate = candidate;
        //                }
        //            }
        //
        //            final ClassTypeInfo superClassType = new ClassTypeInfo(bestCandidate);
        //            for (final UnresolvedTypeInfo<? extends TypeInfo> unresolvedTypeArgument : this
        //                    .getTypeArguments()) {
        //                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
        //                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        //                superClassType.addTypeArgument(typeArgument);
        //            }
        //            this.resolvedInfo = superClassType;
        //            return this.resolvedInfo;
        //        }
    }

    private int getMatchedLength(final String[] array1, final String[] array2) {

        for (int index = 0; true; index++) {

            if (array1.length <= index) {
                return index;
            }

            if (array2.length <= index) {
                return index;
            }

            if (!array1[index].equals(array2[index])) {
                return index;
            }
        }
    }

    /**
     * ���p�\�Ȗ��O��ԁC�^�̊��S�C������^���ď�����
     * @param referenceName �^�̊��S�C����
     */
    public UnresolvedClassTypeInfo(final String[] referenceName) {
        this(new LinkedList<UnresolvedClassImportStatementInfo>(), referenceName);
    }

    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeParameterUsage �ǉ�����^�p�����[�^�g�p
     */
    public final void addTypeArgument(
            final UnresolvedTypeInfo<? extends TypeInfo> typeParameterUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeParameterUsage);
    }

    /**
     * ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List ��Ԃ�
     * 
     * @return ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List
     */
    public final List<UnresolvedTypeInfo<? extends TypeInfo>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * ���̎Q�ƌ^�̖��O��Ԃ�
     * 
     * @return ���̎Q�ƌ^�̖��O��Ԃ�
     */
    @Override
    public final String getTypeName() {
        return this.referenceName[this.referenceName.length - 1];
    }

    /**
     * ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     * 
     * @return ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     */
    public final String[] getReferenceName() {
        return Arrays.<String> copyOf(this.referenceName, this.referenceName.length);
    }

    /**
     * ���̎Q�ƌ^�̎Q�Ɩ��������ŗ^����ꂽ�����Ō������ĕԂ�
     * 
     * @param delimiter �����ɗp���镶��
     * @return ���̎Q�ƌ^�̎Q�Ɩ��������ŗ^����ꂽ�����Ō�������������
     */
    public final String getReferenceName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        final StringBuilder sb = new StringBuilder(this.referenceName[0]);
        for (int i = 1; i < this.referenceName.length; i++) {
            sb.append(delimiter);
            sb.append(this.referenceName[i]);
        }

        return sb.toString();
    }

    /**
     * ���̎Q�ƌ^�̊��S���薼�Ƃ��ĉ\���̂��閼�O��Ԗ��̈ꗗ��Ԃ�
     * 
     * @return ���̎Q�ƌ^�̊��S���薼�Ƃ��ĉ\���̂��閼�O��Ԗ��̈ꗗ
     */
    public final List<UnresolvedClassImportStatementInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * ���̎Q�Ƃ��P�����ǂ�����Ԃ�
     * 
     * @return�@�P���ł���ꍇ��true�C�����łȂ��ꍇ��false
     */
    public final boolean isMoniminalReference() {
        return 1 == this.referenceName.length;
    }

    /**
     * �������N���X��^����ƁC���̖������Q�ƌ^��Ԃ�
     * 
     * @param referencedClass �������N���X
     * @return �^����ꂽ�������N���X�̖������Q�ƌ^
     */
    public final static UnresolvedClassTypeInfo getInstance(UnresolvedClassInfo referencedClass) {
        return new UnresolvedClassTypeInfo(referencedClass.getFullQualifiedName());
    }

    /**
     * ���̖������Q�ƌ^���\���������N���X�Q�Ƃ�Ԃ�
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @return ���̖������Q�ƌ^���\���������N���X�Q��
     */
    public final UnresolvedClassReferenceInfo getUsage(
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        UnresolvedClassReferenceInfo usage = new UnresolvedClassReferenceInfo(
                this.availableNamespaces, this.referenceName);

        usage.setOuterUnit(outerUnit);
        usage.setFromLine(fromLine);
        usage.setFromColumn(fromColumn);
        usage.setToLine(toLine);
        usage.setToColumn(toColumn);

        for (UnresolvedTypeInfo<? extends TypeInfo> typeArgument : this.typeArguments) {
            usage.addTypeArgument(typeArgument);
        }
        return usage;
    }

    /**
     * ���p�\�Ȗ��O��Ԗ���ۑ����邽�߂̕ϐ��C���O���������̍ۂɗp����
     */
    private final List<UnresolvedClassImportStatementInfo> availableNamespaces;

    /**
     * �Q�Ɩ���ۑ�����ϐ�
     */
    private final String[] referenceName;

    /**
     * �^������ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeInfo<? extends TypeInfo>> typeArguments;

    private ReferenceTypeInfo resolvedInfo;

}
