package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * AstVisitStateManager �̊�{�ƂȂ钊�ۃN���X.
 * <p>
 * �r�W�^�[��AST�m�[�h�ւ̓��B�󋵂ɉ����ď�Ԃ̕ۑ��C�������s���d�g�݂�񋟂���.
 * AST�͖؍\���ł��邽�߁C���̃r�W�^�[�̓��B��Ԃ́C����m�[�h�̒��ɓ���C����m�[�h�̒�����o�鎞�ɕω�����ƍl������.
 * ���ɁC�m�[�h�̒��ɏo���肷��ۂɂ́C���ɓ��������ɕω�������Ԃ��C�O�ɏo�鎞�ɕ������Ȃ���΂Ȃ�Ȃ�.
 * �����ŁC���̃N���X�͏�ԕω��̃g���K�ƂȂ�悤��AST�m�[�h�ɓ��鎞�ɃX�^�b�N�Ɍ��݂̏�Ԃ�ۑ����Ă����C
 * �g���K�ƂȂ�悤��AST�m�[�h����o�����ɃX�^�b�N����ߋ��̏�Ԃ����o���ď�Ԃ𕜌�����d�g�݂�񋟂���.
 * ��Ԃ𕜌�����ɂ́C���̎��̏�Ԃ𕜌��ł���悤�ȏ����L�^���Ȃ���΂Ȃ�Ȃ����C�ǂ̂悤�ȏ����L�^���Ȃ���΂Ȃ�Ȃ����́C
 * �T�u�N���X�ɂ���ĈقȂ�ƍl������.
 * �����ŁC�^�p�����[�^T��p���ĔC�ӂɏ���ێ�����^���w�肷�邱�Ƃ�,��Ԃ̋L�^�C�������ɎQ�Ƃ�������T�u�N���X���Ɏw�肷�邱�Ƃ��ł���悤�ɂ��Ă���.
 * 
 * <p>
 * ���̃N���X���p������N���X�� {@link #isStateChangeTriggerEvent(AstToken)},{@link #getState()},
 * {@link #setState(T)}��3�̒��ۃ��\�b�h����������K�v������.
 * <p>
 * 
 * @author kou-tngt
 *
 * @param <T> ��Ԃ𕜌����邽�߂̏���ێ�����^.
 */
public abstract class StackedAstVisitStateManager<T> implements AstVisitStateManager {

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateManager#addStateChangeListener(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener)
     */
    public void addStateChangeListener(final StateChangeListener<AstVisitEvent> listener) {
        this.listeners.add(listener);
    }

    /**
     * �r�W�^�[��AST�m�[�h�̒��ɓ��������̃C�x���g�ʒm���󂯎���āC���݂̏�Ԃ��X�^�b�N�ɋL�^����.
     * 
     * @param event �C�x���g
     */
    public void entered(final AstVisitEvent event) {
        if (this.isStateChangeTriggerEvent(event)) {
            this.pushState();
        }
    }

    /**
     * �r�W�^�[��AST�m�[�h�̒�����o�����̃C�x���g�ʒm���󂯎���āC�X�^�b�N����ߋ��̏�Ԃ����o����������.
     * 
     * @param event �C�x���g
     */
    public void exited(final AstVisitEvent event) {
        if (this.isStateChangeTriggerEvent(event)) {
            this.popState();
        }
    }

    /**
     * �o�^�ς݂̏�ԕω����X�i�[�̃Z�b�g���擾����
     * @return �o�^�ς݂̏�ԕω����X�i�[�̃Z�b�g
     */
    public Set<StateChangeListener<AstVisitEvent>> getListeners() {
        return Collections.unmodifiableSet(this.listeners);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateManager#removeStateChangeListener(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener)
     */
    public void removeStateChangeListener(final StateChangeListener<AstVisitEvent> listener) {
        this.listeners.remove(listener);
    }

    /**
     * AST�̃m�[�h�ɓ��B�����C�x���g���󂯎��.
     * ���B���������ł̓r�W�^�[�̏�ԕω��͔������Ȃ����ߓ��ɉ������Ȃ�.
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitListener#visited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitEvent)
     */
    public final void visited(final AstVisitEvent event) {
        //�������Ȃ�
    }

    /**
     * �o�^�ς݂̏�ԕω����X�i�[�ɑ΂��ď�ԕω��C�x���g��ʒm����.
     * 
     * @param type ��ԕω��̎��
     * @param triggerEvent ��ԕω��̃g���K�ƂȂ���AST���B�C�x���g
     */
    protected final void fireStateChangeEvent(final StateChangeEventType type, final AstVisitEvent triggerEvent) {
        final StateChangeEvent<AstVisitEvent> event = new StateChangeEvent<AstVisitEvent>(this, type,
                triggerEvent);
        for (final StateChangeListener<AstVisitEvent> listener : this.getListeners()) {
            listener.stateChanged(event);
        }
    }

    /**
     * ��Ԃ��X�^�b�N������o���ĕ�������.
     */
    private void popState() {
        this.setState(this.stateStack.pop());
    }

    /**
     * ���݂̏�Ԃ��X�^�b�N�ɓ����.
     */
    private void pushState() {
        this.stateStack.push(this.getState());
    }

    /**
     * ���݂̏�Ԃ̏���Ԃ�.
     * @return ���݂̏�Ԃ̏��
     */
    protected T getState() {
        return this.state;
    }

    /**
     * �����ŗ^����ꂽ������ɏ�Ԃ𕜌�����.
     * @param state ��Ԃ𕜌����邽�߂̏��
     */
    protected void setState(T state) {
        this.state = state;
    }

    /**
     * �����ŗ^����ꂽ�g�[�N������ԕω��̃g���K�ɂȂ蓾�邩�ǂ�����Ԃ�.
     * 
     * @param token ��ԕω��̃g���K�ƂȂ蓾�邩�ǂ����𒲂ׂ�g�[�N��
     * @return ��ԕω��̃g���K�ɂȂ蓾��ꍇ��true
     */
    protected abstract boolean isStateChangeTriggerEvent(AstVisitEvent event);

    /**
     * ��ԕω����X�i�̃Z�b�g
     */
    private final Set<StateChangeListener<AstVisitEvent>> listeners = new LinkedHashSet<StateChangeListener<AstVisitEvent>>();

    /**
     * ��Ԃ��L�^���Ă����X�^�b�N
     */
    private final Stack<T> stateStack = new Stack<T>();
    
    private T state;
}
