package jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArbitraryTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExtendsTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SuperTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.NameResolver;


/**
 * �o�C�g�R�[�h���瓾�����������𖼑O�������邽�߂̃N���X
 * 
 * @author higo
 *
 */
public class JavaByteCodeNameResolver {

    /**
     * ���������O���𖼑O�������郁�\�b�h
     * �����������O��Full Qualified Name��Ԃ�
     * 
     * @param unresolvedName
     * @return
     */
    public static String[] resolveName(final String unresolvedName) {
        return JavaByteCodeUtility.separateName(unresolvedName);
    }

    /**
     * �������^���𖼑O�������郁�\�b�h�D
     * ���C��O�����́CTypeParameter����������ꍇ�̂ݎw�肷��΂悢.
     * �������C��������^�������ɃW�F�l���N�X���܂�ł���ꍇ������̂ŁC
     * ��O�����͂�����Ǝw�肷�邱�Ƃ��d�v
     * ## �������͍폜����܂���.
     * 
     * @param unresolvedType
     * @param thisTypeParameter �^�p�����[�^��extendsType����������Ƃ��̂ݕK�{
     * @param ownerUnit
     * 
     * @return
     */
    public static TypeInfo resolveType(final String unresolvedType,
            final TypeParameterInfo thisTypeParameter, final TypeParameterizable ownerUnit) {

        if (null == unresolvedType) {
            throw new IllegalArgumentException();
        }

        // �ꕶ���Ȃ�΁CprimitiveType��Void�łȂ���΂Ȃ�Ȃ�
        if (1 == unresolvedType.length()) {
            return translateSingleCharacterType(unresolvedType.charAt(0));
        }

        // '['�Ŏn�܂��Ă���Ƃ��͔z��
        else if ('[' == unresolvedType.charAt(0)) {
            final TypeInfo subType = resolveType(unresolvedType.substring(1), thisTypeParameter,
                    ownerUnit);

            // ���Ƃ��Ɣz��Ȃ�Ύ�����1���₷
            if (subType instanceof ArrayTypeInfo) {
                final ArrayTypeInfo subArrayType = (ArrayTypeInfo) subType;
                final TypeInfo ElementType = subArrayType.getElementType();
                final int dimension = subArrayType.getDimension();
                return ArrayTypeInfo.getType(ElementType, dimension + 1);
            }

            //�@�z��łȂ��Ȃ�z��ɂ���
            else {
                return ArrayTypeInfo.getType(subType, 1);
            }
        }

        // �z��łȂ��Q�ƌ^�̏ꍇ
        else if ('L' == unresolvedType.charAt(0)) {

            final ClassInfoManager classInfoManager = DataManager.getInstance()
                    .getClassInfoManager();
            final String[] unresolvedSeparatedType = JavaByteCodeUtility
                    .separateName(unresolvedType.substring(1, unresolvedType.length() - 1));
            final String[] unresolvedSeparatedTypeWithoutTypeArguments = new String[unresolvedSeparatedType.length];
            for (int index = 0; index < unresolvedSeparatedType.length; index++) {
                unresolvedSeparatedTypeWithoutTypeArguments[index] = JavaByteCodeUtility
                        .removeTypeArguments(unresolvedSeparatedType[index]);
            }

            ExternalClassInfo referencedClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(unresolvedSeparatedTypeWithoutTypeArguments);
            if (null == referencedClass) {
                referencedClass = new ExternalClassInfo(unresolvedSeparatedTypeWithoutTypeArguments);
                classInfoManager.add(referencedClass);
            }
            final ClassTypeInfo type = new ClassTypeInfo(referencedClass);

            final String unresolvedTypeArgumentsString = JavaByteCodeUtility
                    .extractTypeArguments(unresolvedSeparatedType[unresolvedSeparatedType.length - 1]);
            if (null != unresolvedTypeArgumentsString) {
                final String[] unresolvedTypeArguments = JavaByteCodeUtility
                        .separateTypes(unresolvedTypeArgumentsString);
                for (final String unresolvedTypeArgument : unresolvedTypeArguments) {
                    final TypeInfo typeArgument = resolveType(unresolvedTypeArgument,
                            thisTypeParameter, ownerUnit);
                    type.addTypeArgument(typeArgument);
                }
            }

            return type;
        }

        // �W�F�l���N�X(TE(�ʂ�E����Ȃ��Ă���������);)�̏ꍇ
        else if ('T' == unresolvedType.charAt(0)) {

            final String identifier = unresolvedType.substring(1, unresolvedType.length() - 1);
            if ((null != thisTypeParameter) && identifier.equals(thisTypeParameter.getName())) {
                return new TypeParameterTypeInfo(thisTypeParameter);
            }
            final List<TypeParameterInfo> availableTypeParameters = NameResolver
                    .getAvailableTypeParameters(ownerUnit);
            for (final TypeParameterInfo typeParameter : availableTypeParameters) {
                if (identifier.equals(typeParameter.getName())) {
                    return new TypeParameterTypeInfo(typeParameter);
                }
            }
        }

        // �W�F�l���N�X(-)�̏ꍇ
        else if ('-' == unresolvedType.charAt(0)) {

            final String unresolvedSuperType = unresolvedType.substring(1);
            final TypeInfo superType = resolveType(unresolvedSuperType, thisTypeParameter,
                    ownerUnit);
            assert superType instanceof ReferenceTypeInfo : "superType must be instanceof ReferenceTypeInfo";
            return new SuperTypeInfo((ReferenceTypeInfo) superType);
        }

        // �W�F�l���N�X(+)�̏ꍇ
        else if ('+' == unresolvedType.charAt(0)) {

            final String unresolvedExtendsType = unresolvedType.substring(1);
            final TypeInfo extendsType = resolveType(unresolvedExtendsType, thisTypeParameter,
                    ownerUnit);
            assert extendsType instanceof ReferenceTypeInfo : "extendsType must be instanceof ReferenceTypeInfo";
            return new ExtendsTypeInfo((ReferenceTypeInfo) extendsType);
        }

        throw new IllegalArgumentException();
    }

    /**
     * �������^�p�����[�^���𖼑O��������N���X.
     * ���C��O�����́CTypeParameter����������ꍇ�̂ݎw�肷��΂悢.
     * �������C��������^�������ɃW�F�l���N�X���܂�ł���ꍇ������̂ŁC
     * ��O�����͂�����Ǝw�肷�邱�Ƃ��d�v
     * 
     * @param unresolvedTypeParameter �������^�̕�����
     * @param index �^�p�����[�^�̃C���f�b�N�X�i���ԁj
     * @param ownerUnit �^�p�����[�^�����L���郆�j�b�g�i�N���X or ���\�b�h or �R���X�g���N�^�j
     * @return
     */
    public static TypeParameterInfo resolveTypeParameter(final String unresolvedTypeParameter,
            final int index, final TypeParameterizable ownerUnit) {

        if ((null == unresolvedTypeParameter) || (null == ownerUnit)) {
            throw new IllegalArgumentException();
        }

        final int firstColonIndex = unresolvedTypeParameter.indexOf(":");

        final String identifier = unresolvedTypeParameter.substring(0, firstColonIndex);
        final TypeParameterInfo typeParameter = new TypeParameterInfo(ownerUnit, identifier, index);

        final String unresolvedExtendsTypes = unresolvedTypeParameter.substring(firstColonIndex);
        for (int startIndex = 0, endIndex = 0, nestLevel = 0; endIndex < unresolvedExtendsTypes
                .length(); endIndex++) {

            if ((':' == unresolvedExtendsTypes.charAt(endIndex)) && (0 == nestLevel)) {
                startIndex = endIndex + 1;
            }

            else if ((';' == unresolvedExtendsTypes.charAt(endIndex)) && (0 == nestLevel)) {
                final String unresolvedExtendsType = unresolvedExtendsTypes.substring(startIndex,
                        endIndex + 1);
                final TypeInfo extendsType = resolveType(unresolvedExtendsType, typeParameter,
                        ownerUnit);
                typeParameter.addExtendsType(extendsType);
            }

            else if ('<' == unresolvedExtendsTypes.charAt(endIndex)) {
                nestLevel++;
            }

            else if ('>' == unresolvedExtendsTypes.charAt(endIndex)) {
                nestLevel--;
            }
        }

        return typeParameter;
    }

    /**
     * �^����ꂽ�ꕶ���^��\�����������ƂɁC�^��\���I�u�W�F�N�g��Ԃ�
     * 
     * @param c
     * @return
     */
    private static TypeInfo translateSingleCharacterType(final char c) {

        switch (c) {
        case 'Z':
            return PrimitiveTypeInfo.BOOLEAN;
        case 'C':
            return PrimitiveTypeInfo.CHAR;
        case 'B':
            return PrimitiveTypeInfo.BYTE;
        case 'S':
            return PrimitiveTypeInfo.SHORT;
        case 'I':
            return PrimitiveTypeInfo.INT;
        case 'F':
            return PrimitiveTypeInfo.FLOAT;
        case 'J':
            return PrimitiveTypeInfo.LONG;
        case 'D':
            return PrimitiveTypeInfo.DOUBLE;
        case 'V':
            return VoidTypeInfo.getInstance();
        case '*':
            return ArbitraryTypeInfo.getInstance();
        default:
            throw new IllegalArgumentException();
        }
    }

}
