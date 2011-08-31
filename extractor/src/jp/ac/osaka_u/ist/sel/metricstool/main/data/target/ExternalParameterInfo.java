package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * �O�����\�b�h�̈�������ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class ExternalParameterInfo extends ParameterInfo {

    /**
     * �����ŗ^����ꂽExpressionInfo�� List ����C�����̌^�� List ���쐬���C�Ԃ�
     * 
     * @param expressions �G���e�B�e�B��List
     * @param ownerMethod ������錾���Ă��郁�\�b�h
     * @return �����̌^�� List
     */
    public static List<ParameterInfo> createParameters(final List<ExpressionInfo> expressions,
            final ExternalMethodInfo ownerMethod) {

        if (null == expressions || null == ownerMethod) {
            throw new NullPointerException();
        }

        final List<ParameterInfo> parameters = new LinkedList<ParameterInfo>();
        for (final ExpressionInfo expression : expressions) {
            final TypeInfo type = expression.getType();
            final ExternalParameterInfo parameter = new ExternalParameterInfo(type, ownerMethod);
            parameters.add(parameter);
        }

        return Collections.unmodifiableList(parameters);
    }

    /**
     * �����ŗ^����ꂽExpressionInfo�� List ����C�����̌^�� List ���쐬���C�Ԃ�
     * 
     * @param expressions �G���e�B�e�B��List
     * @param ownerConstructor ������錾���Ă���R���X�g���N�^
     * @return �����̌^�� List
     */
    public static List<ParameterInfo> createParameters(final List<ExpressionInfo> expressions,
            final ExternalConstructorInfo ownerConstructor) {

        if (null == expressions || null == ownerConstructor) {
            throw new NullPointerException();
        }

        final List<ParameterInfo> parameters = new LinkedList<ParameterInfo>();
        for (final ExpressionInfo expression : expressions) {
            final TypeInfo type = expression.getType();
            final ExternalParameterInfo parameter = new ExternalParameterInfo(type,
                    ownerConstructor);
            parameters.add(parameter);
        }

        return Collections.unmodifiableList(parameters);
    }

    /**
     * �����̌^���w�肵�ăI�u�W�F�N�g���������D�O����`�̃��\�b�h���Ȃ̂ň������͕s���D
     * 
     * @param type �����̌^
     * @param definitionMethod �錾���Ă��郁�\�b�h
     */
    public ExternalParameterInfo(final TypeInfo type, final CallableUnitInfo definitionMethod) {
        super(new HashSet<ModifierInfo>(), UNKNOWN_NAME, type, definitionMethod, 0, 0, 0, 0);
    }

    /**
     * �s���Ȉ�������\���萔
     */
    public final static String UNKNOWN_NAME = "unknown";
}
