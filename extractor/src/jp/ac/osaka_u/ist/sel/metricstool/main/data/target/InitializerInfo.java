package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;


/**
 *�@�C�j�V�����C�U�̋��ʂ̐e�N���X
 *<br>
 *�C�j�V�����C�U�Ƃ́C�X�^�e�B�b�N�E�C�j�V�����C�U��C���X�^���X�E�C�j�V�����C�U�@�Ȃǂł��� 
 * @author g-yamada
 *
 */
@SuppressWarnings("serial")
public abstract class InitializerInfo extends CallableUnitInfo {

    public InitializerInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(Collections.<ModifierInfo> emptySet(), fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public final String getSignatureText() {
        return "";
    }

}
