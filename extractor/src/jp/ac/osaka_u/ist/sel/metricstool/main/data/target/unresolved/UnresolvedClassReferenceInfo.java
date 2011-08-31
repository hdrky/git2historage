package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������N���X�Q�Ƃ�\���N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedClassReferenceInfo extends UnresolvedExpressionInfo<ExpressionInfo> {

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     */
    public UnresolvedClassReferenceInfo(
            final List<UnresolvedClassImportStatementInfo> availableNamespaces,
            final String[] referenceName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.referenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.fullReferenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.qualifierUsage = null;
        this.typeArguments = new LinkedList<UnresolvedTypeInfo<?>>();
    }

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     * @param ownerUsage �e�Q��
     */
    public UnresolvedClassReferenceInfo(
            final List<UnresolvedClassImportStatementInfo> availableNamespaces,
            final String[] referenceName, final UnresolvedClassReferenceInfo ownerUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName) || (null == ownerUsage)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        String[] ownerReferenceName = ownerUsage.getFullReferenceName();
        String[] fullReferenceName = new String[referenceName.length + ownerReferenceName.length];
        System.arraycopy(ownerReferenceName, 0, fullReferenceName, 0, ownerReferenceName.length);
        System.arraycopy(referenceName, 0, fullReferenceName, ownerReferenceName.length,
                referenceName.length);
        this.fullReferenceName = fullReferenceName;
        this.referenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.qualifierUsage = ownerUsage;
        this.typeArguments = new LinkedList<UnresolvedTypeInfo<?>>();
    }

    @Override
    public ExpressionInfo resolve(final TargetClassInfo usingClass,
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

        /*// �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        final String[] referenceName = this.getReferenceName();

        if (this.hasOwnerReference()) {

            final UnresolvedClassReferenceInfo unresolvedClassReference = this.getQualifierUsage();
            ExpressionInfo classReference = unresolvedClassReference.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            assert null != classReference : "null is returned!";

            NEXT_NAME: for (int i = 0; i < referenceName.length; i++) {

                // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                if (classReference.getType() instanceof UnknownTypeInfo) {

                    this.resolvedInfo = new UnknownEntityUsageInfo(referenceName, usingMethod,
                            fromLine, fromColumn, toLine, toColumn);
                    /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                    return this.resolvedInfo;

                    // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                } else if (classReference.getType() instanceof ClassTypeInfo) {

                    final ClassInfo ownerClass = ((ClassTypeInfo) classReference.getType())
                            .getReferencedClass();

                    // �C���i�[�N���X����T���̂ňꗗ���擾
                    final SortedSet<InnerClassInfo> innerClasses = NameResolver
                            .getAvailableDirectInnerClasses(((ClassTypeInfo) classReference
                                    .getType()).getReferencedClass());
                    for (final InnerClassInfo innerClass : innerClasses) {

                        final ClassInfo innerClassInfo = (ClassInfo) innerClass;

                        // ��v����N���X�������������ꍇ
                        if (referenceName[i].equals(innerClassInfo.getClassName())) {
                            // TODO ���p�֌W���\�z����R�[�h���K�v�H

                            // TODO �^�p�����[�^����ǋL���鏈�����K�v
                            final ClassTypeInfo reference = new ClassTypeInfo(innerClassInfo);
                            classReference = new ClassReferenceInfo(reference, usingMethod,
                                    fromLine, fromColumn, toLine, toColumn);
                            /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/
                            continue NEXT_NAME;
                        }
                    }

                    // ������Ȃ��Ă��O���N���X�̏ꍇ�͂��傤���Ȃ�
                    if (ownerClass instanceof ExternalClassInfo) {
                        classReference = new UnknownEntityUsageInfo(referenceName, usingMethod,
                                fromLine, fromColumn, toLine, toColumn);
                        /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/
                        continue NEXT_NAME;
                    }

                    assert false : "Here shouldn't be reached!";
                }

                assert false : "Here shouldn't be reached!";
            }

            this.resolvedInfo = classReference;
            return this.resolvedInfo;

        } else {

            // �������Q�ƌ^�� UnresolvedFullQualifiedNameReferenceTypeInfo �Ȃ�΁C���S���薼�Q�Ƃł���Ɣ��f�ł���
            if (this instanceof UnresolvedFullQualifiedNameClassReferenceInfo) {

                ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null == classInfo) {
                    classInfo = new ExternalClassInfo(referenceName);
                    classInfoManager.add(classInfo);
                }

                // TODO �^�p�����[�^����ǋL���鏈�����K�v
                final ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                this.resolvedInfo = new ClassReferenceInfo(reference, usingMethod, fromLine,
                        fromColumn, toLine, toColumn);
                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                return this.resolvedInfo;
            }

            // �Q�Ɩ������S���薼�ł���Ƃ��Č���
            {
                final ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null != classInfo) {

                    // TODO�@�^�p�����[�^����ǋL���鏈�����K�v
                    final ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                    this.resolvedInfo = new ClassReferenceInfo(reference, usingMethod, fromLine,
                            fromColumn, toLine, toColumn);
                    /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                    return this.resolvedInfo;
                }
            }

            // ���p�\�ȃC���i�[�N���X������T��
            {
                final ClassInfo outestClass;
                if (usingClass instanceof InnerClassInfo) {
                    outestClass = NameResolver.getOuterstClass((InnerClassInfo) usingClass);
                } else {
                    outestClass = usingClass;
                }

                for (final ClassInfo innerClassInfo : ClassInfo.convert(NameResolver
                        .getAvailableInnerClasses(outestClass))) {

                    if (innerClassInfo.getClassName().equals(referenceName[0])) {

                        // availableField.getType() ���玟��word(name[i])�𖼑O����
                        // TODO �^�p�����[�^�����i�[���鏈�����K�v
                        ClassTypeInfo reference = new ClassTypeInfo(innerClassInfo);
                        ExpressionInfo classReference = new ClassReferenceInfo(reference,
                                usingMethod, fromLine, fromColumn, toLine, toColumn);
                        /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/

                        NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                            // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                            if (classReference.getType() instanceof UnknownTypeInfo) {

                                this.resolvedInfo = new UnknownEntityUsageInfo(referenceName,
                                        usingMethod, fromLine, fromColumn, toLine, toColumn);
                                /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
                                return this.resolvedInfo;

                                // �e���N���X�^�̏ꍇ
                            } else if (classReference.getType() instanceof ClassTypeInfo) {

                                final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                        .getType()).getReferencedClass();

                                // �C���i�[�N���X����T���̂ňꗗ���擾
                                final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                        .getAvailableDirectInnerClasses(((ClassTypeInfo) classReference
                                                .getType()).getReferencedClass());
                                for (final ClassInfo innerClass : ClassInfo.convert(innerClasses)) {

                                    // ��v����N���X�������������ꍇ
                                    if (referenceName[i].equals(innerClass.getClassName())) {
                                        // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                        // TODO�@�^�p�����[�^�����i�[���鏈�����K�v
                                        reference = new ClassTypeInfo(innerClass);
                                        classReference = new ClassReferenceInfo(reference,
                                                usingMethod, fromLine, fromColumn, toLine, toColumn);
                                        /*classReference
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        continue NEXT_NAME;
                                    }
                                }

                                // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                if (ownerClass instanceof ExternalClassInfo) {

                                    classReference = new UnknownEntityUsageInfo(referenceName,
                                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                                    /*classReference
                                            .setOwnerExecutableElement(ownerExecutableElement);*/
                                    continue NEXT_NAME;
                                }
                            }

                            assert false : "Here shouldn't be reached!";
                        }

                        this.resolvedInfo = classReference;
                        return this.resolvedInfo;
                    }
                }
            }

            // ���p�\�Ȗ��O��Ԃ���^����T��
            {
                for (final UnresolvedClassImportStatementInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    // ���O��Ԗ�.* �ƂȂ��Ă���ꍇ
                    if (availableNamespace.isAll()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // ���O��Ԃ̉��ɂ���e�N���X�ɑ΂���
                        for (final ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {

                            // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                            final String className = classInfo.getClassName();
                            if (className.equals(referenceName[0])) {

                                // availableField.getType() ���玟��word(name[i])�𖼑O����
                                // TODO �^�p�����[�^�����i�[���鏈�����K�v
                                ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                                ExpressionInfo classReference = new ClassReferenceInfo(reference,
                                        usingMethod, fromLine, fromColumn, toLine, toColumn);
                                /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/

                                NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                    // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                    if (classReference.getType() instanceof UnknownTypeInfo) {

                                        this.resolvedInfo = new UnknownEntityUsageInfo(
                                                referenceName, usingMethod, fromLine, fromColumn,
                                                toLine, toColumn);
                                        /*this.resolvedInfo
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        return this.resolvedInfo;

                                        // �e���N���X�^�̏ꍇ
                                    } else if (classReference.getType() instanceof ClassTypeInfo) {

                                        final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                                .getType()).getReferencedClass();

                                        // �C���i�[�N���X����T���̂ňꗗ���擾
                                        final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses(((ClassTypeInfo) classReference
                                                        .getType()).getReferencedClass());
                                        for (final ClassInfo innerClass : ClassInfo
                                                .convert(innerClasses)) {

                                            // ��v����N���X�������������ꍇ
                                            if (referenceName[i].equals(innerClass.getClassName())) {
                                                // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                // TODO �^�p�����[�^�����i�[���鏈�����K�v
                                                reference = new ClassTypeInfo(innerClass);
                                                classReference = new ClassReferenceInfo(reference,
                                                        usingMethod, fromLine, fromColumn, toLine,
                                                        toColumn);
                                                /*classReference
                                                        .setOwnerExecutableElement(ownerExecutableElement);*/
                                                continue NEXT_NAME;
                                            }
                                        }

                                        // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                        if (ownerClass instanceof ExternalClassInfo) {

                                            classReference = new UnknownEntityUsageInfo(
                                                    referenceName, usingMethod, fromLine,
                                                    fromColumn, toLine, toColumn);
                                            /*classReference
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            continue NEXT_NAME;
                                        }
                                    }

                                    assert false : "Here shouldn't be reached!";
                                }

                                this.resolvedInfo = classReference;
                                return this.resolvedInfo;
                            }
                        }

                        // ���O���.�N���X�� �ƂȂ��Ă���ꍇ
                    } else {

                        final String[] importName = availableNamespace.getImportName();

                        // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                        if (importName[importName.length - 1].equals(referenceName[0])) {

                            ClassInfo specifiedClassInfo = classInfoManager
                                    .getClassInfo(importName);
                            if (null == specifiedClassInfo) {
                                specifiedClassInfo = new ExternalClassInfo(importName);
                                classInfoManager.add(specifiedClassInfo);
                            }

                            // TODO �^�p�����[�^�����i�[���鏈�����K�v
                            ClassTypeInfo reference = new ClassTypeInfo(specifiedClassInfo);
                            ExpressionInfo classReference = new ClassReferenceInfo(reference,
                                    usingMethod, fromLine, fromColumn, toLine, toColumn);
                            /*classReference.setOwnerExecutableElement(ownerExecutableElement);*/

                            NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                if (classReference.getType() instanceof UnknownTypeInfo) {

                                    this.resolvedInfo = new UnknownEntityUsageInfo(referenceName,
                                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                                    /*this.resolvedInfo
                                            .setOwnerExecutableElement(ownerExecutableElement);*/
                                    return this.resolvedInfo;

                                    // �e���N���X�^�̏ꍇ
                                } else if (classReference.getType() instanceof ClassTypeInfo) {

                                    final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                            .getType()).getReferencedClass();

                                    // �C���i�[�N���X�ꗗ���擾
                                    final SortedSet<InnerClassInfo> innerClasses = NameResolver
                                            .getAvailableDirectInnerClasses(((ClassTypeInfo) classReference
                                                    .getType()).getReferencedClass());
                                    for (final ClassInfo innerClass : ClassInfo
                                            .convert(innerClasses)) {

                                        // ��v����N���X�������������ꍇ
                                        if (referenceName[i].equals(innerClass.getClassName())) {
                                            // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                            // TODO �^�p�����[�^�����i�[���鏈�����K�v
                                            reference = new ClassTypeInfo(innerClass);
                                            classReference = new ClassReferenceInfo(reference,
                                                    usingMethod, fromLine, fromColumn, toLine,
                                                    toColumn);
                                            /*classReference
                                                    .setOwnerExecutableElement(ownerExecutableElement);*/
                                            continue NEXT_NAME;
                                        }
                                    }

                                    // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                    if (ownerClass instanceof ExternalClassInfo) {

                                        classReference = new UnknownEntityUsageInfo(referenceName,
                                                usingMethod, fromLine, fromColumn, toLine, toColumn);
                                        /*classReference
                                                .setOwnerExecutableElement(ownerExecutableElement);*/
                                        continue NEXT_NAME;
                                    }
                                }

                                assert false : "Here shouldn't be reached!";
                            }

                            this.resolvedInfo = classReference;
                            return this.resolvedInfo;
                        }
                    }
                }
            }
        }

        /*
         * if (null == usingMethod) { err.println("Remain unresolved \"" +
         * reference.getReferenceName(Settings.getLanguage().getNamespaceDelimiter()) + "\"" + " on
         * \"" + usingClass.getFullQualifiedtName(LANGUAGE.JAVA.getNamespaceDelimiter())); } else {
         * err.println("Remain unresolved \"" +
         * reference.getReferenceName(Settings.getLanguage().getNamespaceDelimiter()) + "\"" + " on
         * \"" + usingClass.getFullQualifiedtName(LANGUAGE.JAVA.getNamespaceDelimiter()) + "#" +
         * usingMethod.getMethodName() + "\"."); }
         */

        // ������Ȃ������ꍇ�́CUknownTypeInfo ��Ԃ�
        this.resolvedInfo = new UnknownEntityUsageInfo(referenceName, usingMethod, fromLine,
                fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/
        return this.resolvedInfo;
    }

    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeArgument �ǉ�����^�p�����[�^�g�p
     */
    public final void addTypeArgument(final UnresolvedTypeInfo<?> typeArgument) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArgument) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeArgument);
    }

    /**
     * ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List ��Ԃ�
     * 
     * @return ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List
     */
    public final List<UnresolvedTypeInfo<?>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * ���̎Q�ƌ^��owner���܂߂��Q�Ɩ���Ԃ�
     * 
     * @return ���̎Q�ƌ^��owner���܂߂��Q�Ɩ���Ԃ�
     */
    public final String[] getFullReferenceName() {
        return Arrays.<String> copyOf(this.fullReferenceName, this.fullReferenceName.length);
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
     * ���̎Q�ƌ^���������Ă��関�����Q�ƌ^��Ԃ�
     * 
     * @return ���̎Q�ƌ^���������Ă��関�����Q�ƌ^
     */
    public final UnresolvedClassReferenceInfo getQualifierUsage() {
        return this.qualifierUsage;
    }

    /**
     * ���̎Q�ƌ^���C���̎Q�ƌ^�ɂ������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �������Ă���ꍇ�� true�C�������Ă��Ȃ��ꍇ�� false
     */
    public final boolean hasOwnerReference() {
        return null != this.qualifierUsage;
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
     * �����ŗ^����ꂽ�������^����\�������ς݌^���N���X�𐶐�����D �����ň����Ƃ��ė^������̂́C�\�[�X�R�[�h���p�[�X����Ă��Ȃ��^�ł���̂ŁC������������ς݌^���N���X��
     * ExternalClassInfo �ƂȂ�D
     * 
     * @param unresolvedReferenceType �������^���
     * @return �����ς݌^���
     */
    public static ExternalClassInfo createExternalClassInfo(
            final UnresolvedClassReferenceInfo unresolvedReferenceType) {

        if (null == unresolvedReferenceType) {
            throw new IllegalArgumentException();
        }

        // �������N���X���̎Q�Ɩ����擾
        final String[] referenceName = unresolvedReferenceType.getReferenceName();

        // ���p�\�Ȗ��O��Ԃ��������C�������N���X���̊��S���薼������
        for (final UnresolvedClassImportStatementInfo availableNamespace : unresolvedReferenceType
                .getAvailableNamespaces()) {

            // ���O��Ԗ�.* �ƂȂ��Ă���ꍇ�́C�����邱�Ƃ��ł��Ȃ�
            if (availableNamespace.isAll()) {
                continue;
            }

            // ���O���.�N���X�� �ƂȂ��Ă���ꍇ
            final String[] importName = availableNamespace.getImportName();

            // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
            if (importName[importName.length - 1].equals(referenceName[0])) {

                final String[] namespace = availableNamespace.getNamespace();
                final String[] fullQualifiedName = new String[namespace.length
                        + referenceName.length];
                System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
                System.arraycopy(referenceName, 0, fullQualifiedName, namespace.length,
                        referenceName.length);

                final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
                return classInfo;
            }
        }

        // ������Ȃ��ꍇ�́C���O��Ԃ� UNKNOWN �� �O���N���X�����쐬
        final ExternalClassInfo unknownClassInfo = new ExternalClassInfo(
                referenceName[referenceName.length - 1]);
        return unknownClassInfo;
    }

    /**
     * �������Q�ƌ^��^����ƁC���̖������N���X�Q�Ƃ�Ԃ�
     * 
     * @param referenceType �������Q�ƌ^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @return �������N���X�Q��
     */
    public final static UnresolvedClassReferenceInfo createClassReference(
            final UnresolvedClassTypeInfo referenceType,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        final UnresolvedClassReferenceInfo reference = new UnresolvedClassReferenceInfo(
                referenceType.getAvailableNamespaces(), referenceType.getReferenceName());
        reference.setOuterUnit(outerUnit);
        reference.setFromLine(fromLine);
        reference.setFromColumn(fromColumn);
        reference.setToLine(toLine);
        reference.setToColumn(toColumn);

        return reference;
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
     * owner���܂߂��Q�Ɩ���ۑ�����ϐ�
     */
    private final String[] fullReferenceName;

    /**
     * ���̎Q�Ƃ��������Ă��関�����Q�ƌ^��ۑ�����ϐ�
     */
    private final UnresolvedClassReferenceInfo qualifierUsage;

    /**
     * �������^�p�����[�^�g�p��ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeInfo<?>> typeArguments;

}
