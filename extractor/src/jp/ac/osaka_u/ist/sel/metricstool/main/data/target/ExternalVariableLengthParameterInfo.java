package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


@SuppressWarnings("serial")
public class ExternalVariableLengthParameterInfo extends ExternalParameterInfo implements
        VariableLengthParameterInfo {

    /**
     * �����̌^���w�肵�ăI�u�W�F�N�g���������D�O����`�̃��\�b�h���Ȃ̂ň������͕s���D
     * 
     * @param type �����̌^
     * @param definitionMethod �錾���Ă��郁�\�b�h
     */
    public ExternalVariableLengthParameterInfo(final TypeInfo type,
            final CallableUnitInfo definitionMethod) {
        super(new ArrayTypeInfo(type, 1), definitionMethod);
    }
}
