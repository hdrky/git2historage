package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.AstVisitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;


/**
 * AST��ŕ����̏�Ԃɕ����Ē�`�����f�[�^���\�z����N���X
 * �e��Ԃɉ������\�z�������s���C�ŏI�I��1�̃f�[�^���\�z����
 * 
 * @author kou-tngt, t-miyake
 *
 * @param <T> �\�z�����f�[�^�̌^
 */
public abstract class StateDrivenDataBuilder<T> extends DataBuilderAdapter<T> implements
        StateChangeListener<AstVisitEvent> {

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilderAdapter#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(final AstVisitEvent e) {
        if (isActive()) {
            for (final AstVisitListener listener : this.stateManagers) {
                listener.entered(e);
            }
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilderAdapter#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent e) throws ASTParseException {
        if (isActive()) {
            for (final AstVisitListener listener : this.stateManagers) {
                listener.exited(e);
            }
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener#stateChangend(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    public abstract void stateChanged(StateChangeEvent<AstVisitEvent> event);

    /**
     * AST�̉�͏�Ԃ��̃}�l�[�W���[��ǉ�
     * @param stateManager 
     */
    protected final void addStateManager(AstVisitStateManager stateManager) {
        //���F���̃��\�b�h��final�C���q�͐�΂ɊO���Ă͂Ȃ�Ȃ��D

        this.stateManagers.add(stateManager);
        stateManager.addStateChangeListener(this);
    }

    /**
     * AST�̉�͏�Ԃ̃}�l�[�W���[��ۑ�����t�B�[���h
     */
    private final Set<AstVisitStateManager> stateManagers = new LinkedHashSet<AstVisitStateManager>();
}
