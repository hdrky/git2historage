package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * AST�r�W�^�[�̉��炩�̋L�q���ւ̏o������Ǘ����钊�ۃX�e�[�g�}�l�[�W���D
 * <p>
 * �L�q�����ċA�I�ȍ\���ɂȂ��Ă���ꍇ�ɂ��Ή����Ă���C
 * �r�W�^�[�����̋L�q���̉��K�w�ڂɂ��邩�Ƃ��������Ǘ�����D
 * <p>
 * ���̃N���X�̃T�u�N���X�́C {@link #getEnterEventType()}, {@link #getExitEventType()}, {@link #isStateChangeTriggerEvent(AstToken)}
 * ��3�̒��ۃ��\�b�h����������K�v������
 * 
 * @author kou-tngt
 *
 */
public abstract class EnterExitStateManager extends StackedAstVisitStateManager {

    /**
     * �r�W�^�[��AST�m�[�h�̒��ɓ��������̃C�x���g�ʒm���󂯎��C
     * ���ꂪ��ԕω��̂��������ɂȂ�m�[�h�ł���΁C���̃m�[�h���ɓ���C�x���g�𔭍s����D
     * 
     * �ǂ̂悤�ȃm�[�h����ԕω��̃g���K�ƂȂ邩�� {@link #isStateChangeTriggerEvent(AstToken)}���\�b�h�ɂ���Č��肳���D
     * �܂��C���s����C�x���g�̃^�C�v�� {@link #getEnterEventType()}���\�b�h�ɂ���ĕԂ������̂�p����D
     * 
     * @param e AST�r�W�^�[�̃r�W�b�g�C�x���g
     */
    @Override
    public void entered(final AstVisitEvent e) {
        super.entered(e);

        if (this.isStateChangeTriggerEvent(e)) {
            this.enterDepthCount++;
            this.fireStateChangeEvent(this.getEnterEventType(), e);
        }
    }

    /**
     * �r�W�^�[��AST�m�[�h�̒�����o�����̃C�x���g�ʒm���󂯎��C
     * ���ꂪ��ԕω��̂��������ɂȂ�m�[�h�ł���΁C���̃m�[�h����o��C�x���g�𔭍s����D
     * 
     * �ǂ̂悤�ȃm�[�h����ԕω��̃g���K�ƂȂ邩�� {@link #isStateChangeTriggerEvent(AstToken)}���\�b�h�ɂ���Č��肳���D
     * �܂��C���s����C�x���g�̃^�C�v�� {@link #getEnterEventType()}���\�b�h�ɂ���ĕԂ������̂�p����D
     * 
     * @param e AST�r�W�^�[�̃r�W�b�g�C�x���g
     */
    @Override
    public void exited(final AstVisitEvent e) {
        super.exited(e);

        if (this.isStateChangeTriggerEvent(e)) {
            this.enterDepthCount--;
            this.fireStateChangeEvent(this.getExitEventType(), e);
        }
    }

    /**
     * �r�W�^�[���Ή�����L�q���̓����ɂ��邩�ǂ�����Ԃ��D
     * 
     * @return�@�r�W�^�[���Ή�����L�q���̒��ɂ����true
     */
    public boolean isEntered() {
        return 0 < this.enterDepthCount;
    }

    /**
     * �Ή�����L�q���̒��ɓ��������ɁC����ɂ���ԕω���ʒm���邽�߂̏�ԕω��C�x���g�̎�ނ�Ԃ����ۃ��\�b�h�D
     * �T�u�N���X�͂��̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C�ӂ̎�ނ̏�ԕω��C�x���g�𑗐M�����邱�Ƃ��ł���D
     * @return�@�Ή�����L�q���̒��ɓ��������ɒʒm������ԕω��C�x���g�̎��
     */
    public abstract StateChangeEventType getEnterEventType();

    /**
     * �Ή�����L�q������o�����ɁC����ɂ���ԕω���ʒm���邽�߂̏�ԕω��C�x���g�̎�ނ�Ԃ����ۃ��\�b�h�D
     * �T�u�N���X�͂��̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C�ӂ̎�ނ̏�ԕω��C�x���g�𑗐M�����邱�Ƃ��ł���D
     * @return�@�Ή�����L�q������o�����ɒʒm������ԕω��C�x���g�̎��
     */
    public abstract StateChangeEventType getExitEventType();

    /**
     * ���݂̏�Ԃ̏���Ԃ�.
     * ���̒��ۃN���X�͏�Ԃ������Ȃ�����null��Ԃ��D
     * @return null
     */
    @Override
    protected Object getState() {
        return null;
    }

    /**
     * �r�W�^�[���Ή�����L�q���̉��K�w�ڂɂ��邩��Ԃ��D
     * 
     * @return�@�r�W�^�[���Ή�����L�q�����ɂ��Ȃ����0�C�L�q�����ɋ���ꍇ�͂��̊K�w��Ԃ��D
     */
    protected int getEnterDepthCount() {
        return this.enterDepthCount;
    }

    /**
     * �����ŗ^����ꂽ�C�x���g���Ή�����L�q���֏o����̃g���K���ǂ�����Ԃ����ۃ��\�b�h.
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�T�u�N���X���ǂ̋L�q���ւ̏o����ɑΉ����邩���w�肷�邱�Ƃ��ł���D
     * 
     * @param event �L�q���֏o����̃g���K���ǂ����𒲂ׂ�C�x���g
     * @return �L�q���֏o����̃g���K�̏ꍇ��true
     */
    @Override
    protected abstract boolean isStateChangeTriggerEvent(AstVisitEvent event);

    /**
     * �����ŗ^����ꂽ������ɏ�Ԃ𕜌�����.
     * ���̒��ۃN���X�͏�Ԃ������Ȃ����߉������Ȃ��D
     * @param state ��Ԃ𕜌����邽�߂̏��
     */
    @Override
    protected void setState(final Object state) {
        //�������Ȃ�
    }

    /**
     * �Ή�����L�q���̌��݂̊K�w��
     */
    private int enterDepthCount;
}
