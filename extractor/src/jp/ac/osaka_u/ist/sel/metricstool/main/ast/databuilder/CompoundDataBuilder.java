package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �����̃r���_��g�ݍ��킹�āC�����I�ȏ����\�z���钊�ۃN���X�D
 * ���O�ɓo�^���ꂽ�r���_�ɑ΂���C�x���g�ʒm�����̑�s���s���D
 * 
 * @author kou-tngt
 *
 * @param <T> ���̃N���X���p�������N���X���\�z����f�[�^�̌^
 */
public abstract class CompoundDataBuilder<T> extends StateDrivenDataBuilder<T> {

    /**
     * �C�x���g�̒ʒm���������r���_��o�^����D
     * �o�^���ꂽ�r���_�͏�����ԂŔ�A�N�e�B�u������邽�߁C�e�T�u�N���X�́C�C�ӂ̃^�C�~���O�œK�؂Ƀr���_���A�N�e�B�u�ɂ��Ȃ���΂Ȃ�Ȃ��D
     * �T�u�N���X�̃R���X�g���N�^����Ăяo����邱�Ƃ��l�����Ă���̂ŁCfinal���O���Ă͂Ȃ�Ȃ��D
     * 
     * @param builder�@�o�^����r���_
     */
    public final void addInnerBuilder(final DataBuilder<?> builder) {
        if (null != builder) {
            builder.deactivate();
            this.builders.add(builder);
        }
    }

    /**
     * AST�m�[�h���C�ӂ̃m�[�h�̒��ɓ��������ɌĂяo�����D
     * �܂���ԊǗ����s���e�N���X�� {@link StateDrivenDataBuilder#entered(AstVisitEvent)} ���\�b�h���Ăяo���C
     * ��ԕω�����������ŁC�A�N�e�B�u��Ԃɂ���r���_�ɑ΂��ăC�x���g��ʒm����D
     * @param e �r�W�^�[��AST�r�W�b�g�C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(final AstVisitEvent e) {
        super.entered(e);

        if (this.isActive()) {
            for (final DataBuilder<?> builder : this.builders) {
                builder.entered(e);
            }
        }
    }

    /**
     * AST�m�[�h���C�ӂ̃m�[�h����o�����ɌĂяo�����D
     * ��ɃA�N�e�B�u��Ԃɂ���r���_�ɑ΂��ăC�x���g��ʒm������ɁC
     * �e�N���X�� {@link StateDrivenDataBuilder#entered(AstVisitEvent)} ���\�b�h���Ăяo����ԕω��𔭐�������D
     * @param e �r�W�^�[��AST�r�W�b�g�C�x���g
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent e) throws ASTParseException {
        if (this.isActive()) {
            for (final DataBuilder<?> builder : this.builders) {
                builder.exited(e);
            }
        }

        super.exited(e);
    }

    /**
     * �o�^���ꂽ�����r���_���폜����
     * 
     * @param builder�@�폜����r���_
     */
    public final void removeInnerBuilder(final DataBuilder<?> builder) {
        this.builders.remove(builder);
    }

    /**
     * �r�����̏�Ԃ����Z�b�g����D
     * �S�Ă̓����r���_�̏�Ԃ����Z�b�g����D
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilderAdapter#reset()
     */
    @Override
    public void reset() {
        super.reset();
        for (final DataBuilder<?> builder : this.builders) {
            builder.reset();
        }
    }

    /**
     * ���p��������r�����̃Z�b�g
     */
    private final Set<DataBuilder<?>> builders = new LinkedHashSet<DataBuilder<?>>();
}
