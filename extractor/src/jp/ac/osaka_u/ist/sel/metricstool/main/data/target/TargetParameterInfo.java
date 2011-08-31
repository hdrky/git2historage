package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * �Ώۃ��\�b�h�̈�����\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class TargetParameterInfo extends ParameterInfo {

    /**
     * �������C�����̌^��^���ăI�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param name ������
     * @param type �����̌^
     * @param index ���Ԗڂ̈�������\��
     * @param definitionMethod �錾���Ă��郁�\�b�h
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetParameterInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final int index, final CallableUnitInfo definitionMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(modifiers, name, type, definitionMethod, fromLine, fromColumn, toLine, toColumn);
        this.index = index;
    }

    /**
     * �����̃C���f�b�N�X��Ԃ�
     * 
     * @return�@�����̃C���f�b�N�X
     */
    public final int getIndex() {
        return this.index;
    }

    private final int index;
}
