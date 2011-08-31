package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolved�ȕϐ��̋��ʂȐe�N���X.
 * <ul>
 * <li>�ϐ���</li>
 * <li>�^</li>
 * <li>�C���q</li>
 * <li>�ʒu���</li>
 * </ul>
 * 
 * @author higo
 * @param <TVar> �����ς݂̌^
 * @param <TUnit> ���̕ϐ���錾���Ă��郆�j�b�g
 */
public abstract class UnresolvedVariableInfo<TVar extends VariableInfo<? extends UnitInfo>, TUnit extends UnresolvedUnitInfo<? extends UnitInfo>>
        extends UnresolvedUnitInfo<TVar> implements ModifierSetting {

    /**
     * �ϐ�����Ԃ�
     * 
     * @return �ϐ���
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �ϐ������Z�b�g����
     * 
     * @param name �ϐ���
     */
    public final void setName(final String name) {

        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * �ϐ��̌^��Ԃ�
     * 
     * @return �ϐ��̌^
     */
    public final UnresolvedTypeInfo<?> getType() {
        return this.type;
    }

    /**
     * �ϐ��̌^���Z�b�g����
     * 
     * @param type �ϐ��̌^
     */
    public final void setType(final UnresolvedTypeInfo<?> type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * ���̕ϐ���錾���Ă��郆�j�b�g��Ԃ�
     * 
     * @return ���̕ϐ���錾���Ă��郆�j�b�g
     */
    public final TUnit getDefinitionUnit() {
        return this.definitionUnit;
    }

    /**
     * �C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    public final void addModifier(final ModifierInfo modifier) {

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * �ϐ��I�u�W�F�N�g������������D
     * 
     * @param name �ϐ���
     * @param type �ϐ��̌^
     * @param definitionUnit �錾���Ă�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    UnresolvedVariableInfo(final String name, final UnresolvedTypeInfo<?> type,
            final TUnit definitionUnit, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super();

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == type) || (null == definitionUnit)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
        this.definitionUnit = definitionUnit;

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * �ϐ�����\���ϐ�
     */
    private String name;

    /**
     * �ϐ��̌^��\���ϐ�
     */
    private UnresolvedTypeInfo<?> type;

    /**
     * �ϐ��̏C���q��\���ϐ�
     */
    private Set<ModifierInfo> modifiers;

    /**
     * �ϐ���錾���Ă��郆�j�b�g��\���ϐ�
     */
    private final TUnit definitionUnit;

}
