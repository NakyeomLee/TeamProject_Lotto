import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

// 기능 모음 창
// 작성자 : 박무현
// 구성 : 로또 프로그램의 전반적인 기능들을 메소드로 작성 및 구현
public class FunctionList extends JFrame {
	
	private FontHolder fontHolder = new FontHolder();

	// 실행시 7개의 중복되지 않는 번호를 list로 반환합니다.
	// 마지막 번호는 보너스 번호로 활용하시면 되며 보너스 번호는 출력메세지나 이미지 파일을 따로 구분하여 작성해주세요.
	public List<String> resultLottoNumber() { 

		List<Integer> list = new ArrayList<>(); // 당첨 번호들끼리 비교하기 위한 리스트
		List<String> result = new ArrayList<>(); // 실질적으로 당첨 번호들을 반환하는 리스트

		while (list.size() < 7) { 
			Random random = new Random();
			Integer num = random.nextInt(45) + 1;

			// 랜덤으로 받는 당첨 번호들 중복 방지
			if (!list.contains(num)) { 
				list.add(num);
				result.add(String.valueOf(num));
			}
		}
		return result;
	}

	// 순차적으로 번호를 보여줄 수 있도록 visible이 false인 Label들을 true로 일정 시간마다 바꾸어주는 메소드 입니다.
	// resultLottoNumber()메소드를 활용하여 7개의 Label(당첨번호)을 구성한 뒤
	// 첫 Label에는 결과메세지를 출력할 Label을 넣어주고,
	// 이후 순차적으로 보여줄 7개의 Label을 순서대로 괄호 안에 넣어주세요.
	// 마지막에는 모든 Label이 출력된 후 visible을 true로 변환시킬 버튼을 넣어주세요.
	public void changeToLabelVisible(JLabel textLbl, JLabel lbl1, JLabel lbl2, JLabel lbl3, JLabel lbl4, JLabel lbl5,
			JLabel lbl6, JLabel plusLbl, JLabel bonusLbl, JButton btn, JButton skipBtn) {

		List<JLabel> list = new ArrayList<>(Arrays.asList(lbl1, lbl2, lbl3, lbl4, lbl5, lbl6, plusLbl, bonusLbl));

		btn.setVisible(false); // 결과 확인하기 버튼 (당첨 번호 확인 페이지)

		// 당첨 번호 레이블들 (리스트에 넣어놓음)
		for (JLabel lbl : list) {
			lbl.setVisible(false); // 타이머에 의해서 하나씩 나타날거라서 처음엔 안 보이게
		}

		list.get(0).setVisible(true); // 첫번째 당첨 번호는 당첨번호 확인 페이지로 넘어오자마자 바로 보이게

		// 타이머 (당첨 번호가 적혀있는 공이 하나씩 띄워질 수 있도록), 0.6초마다 하나씩
		Timer timer = new Timer(600, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (JLabel lbl : list) {
					if (!lbl.isVisible()) {
						lbl.setVisible(true);
						break;
					}
					boolean allVisible = true; // 첫번째 공부터 보너스 번호 공까지 7개가 다 나타났을때 allVisible의 값을 변동시키지 않음

					for (JLabel lbl1 : list) {
						if (!lbl1.isVisible()) {
							allVisible = false;
							break;
						}
					}
					if (allVisible) {
						textLbl.setText("결과를 확인해주세요");
						skipBtn.setVisible(false);
						btn.setVisible(true);
						((Timer) arg0.getSource()).stop(); // 타이머 멈춤
					}
				}
			}
		});
		timer.start(); // 타이머 스타트
	}

	// 자동 , 반자동, 수동 버튼을 눌렀을 때의 구현 기능 메소드 입니다.
	// 괄호 안에는 makeTimer 메소드를 통해 반환 받은 타이머, 버튼, 체크박스List, 옵션,
	// (합의한 내용을 토대로 만든 Integer리스트(버튼 번호 + 자동숫자)), OX레이블 을 넣습니다.
	// 옵션은 String(문자열)로 작성하시면 되고, auto를 입력하시면 자동버튼의 기능,
	// semiAuto를 입력하시면 반자동버튼의 기능을 가집니다.
	// self를 입력하면 수동버튼 기능을 가집니다.
	public void autoOrSemiAutoBtnFuntion(Timer timer, JButton btn, List<JCheckBox> checkBoxList, String option,
			List<Integer> findBtn, JLabel oxLabel) {

		if (option.equals("auto")) { // 자동 버튼

			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					autoChoose(timer, checkBoxList, findBtn, oxLabel); // 전체 자동 버튼에도 활용할 수 있도록 메소드 생성해서 호출
				}
			});

		} else if (option.equals("semiAuto")) { // 반자동 버튼

			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// 체크박스 선택을 감시하기 위한 타이머 켜짐 (번호 6개 선택을 완료하면 선택된 체크박스 이외에 나머지를 비활성화시켜야하기때문)
					timer.start();
					
					int check = 0;

					for (int i = 0; i < checkBoxList.size(); i++) {
						if (checkBoxList.get(i).isEnabled()) { // 체크박스 활성화 상태
							check++;
						}
					}

					if (check == 0) { // 모든 체크박스가 비활성화 상태라면
						for (JCheckBox box : checkBoxList) {
							box.setSelected(false); // 모든 체크박스 선택 해제
						}
						changecheckBoxEnableToTrue(checkBoxList);
						oxLabel.setText("X"); // 사용자가 번호 6개를 제대로 선택 안 한 상태

					} else {
						int count = 0;

						for (int i = 0; i < checkBoxList.size(); i++) {
							if (checkBoxList.get(i).isSelected()) {
								count++;
							}
						}
						List<Integer> list = selectCheckBox(checkBoxList, count);
						listComposition(findBtn, list, 3);
						oxLabel.setText("O"); // 사용자가 번호 6개를 모두 선택한 상태
					}
				}
			});

		} else if (option.equals("self")) { // 수동 버튼
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 체크박스 선택을 감시하기 위한 타이머 켜짐 (번호 6개 선택을 완료하면 선택된 체크박스 이외에 나머지를 비활성화시켜야하기때문)
					timer.start(); 
					
					changecheckBoxEnableToTrue(checkBoxList);

					for (int i = 0; i < checkBoxList.size(); i++) {
						checkBoxList.get(i).setSelected(false); // 체크 박스 선택 해제
					}
					findBtn.clear(); // 이전 기록과 상관없이 다른 버튼을 누르면 각 버튼의 역할을 알려주는 리스트를 비움 (다시 구성하기위해)
					findBtn.add(2);
					oxLabel.setText("X");
				}
			});
		}
	}

	// 전체 자동 버튼 기능 구현을 위해
	// 자동 버튼 기능 코드 내용을 메소드로 세분화 하였습니다.
	public void autoChoose(Timer timer, List<JCheckBox> checkBoxList, List<Integer> findBtn, JLabel oxLabel) {
		// 여기서 타이머는 번호 선택을 감시하는 역할
		// 자동 버튼을 누르면 번호가 6개가 한꺼번에 선택이 됨
		// 모든 체크박스들을 비활성화 시켜야 하나, 타이머가 켜져있으면 모든 체크박스들을 비활성화 시킬 수 없기 때문에 타이머를 멈춤
		timer.stop();
		
		for (int i = 0; i < checkBoxList.size(); i++) {
			checkBoxList.get(i).setSelected(false); // 모든 체크박스 해제
			checkBoxList.get(i).setEnabled(false); // 모든 체크박스 비활성화
		}

		List<Integer> list = selectCheckBox(checkBoxList, 0); // 자동으로 선택된 번호들을 가지고 있는 리스트(자동은 랜덤 선택)
		listComposition(findBtn, list, 1);
		oxLabel.setText("O");
	}

	// 체크박스 리스트를 집어넣으면, 체크가 되어 있는 체크박스들의 숫자만 추려서
	// Integer형의 새로운 리스트로 반환하는 메소드입니다.
	public List<Integer> returnCheckBoxListToIntegerList(List<JCheckBox> list) {
		List<Integer> result = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isSelected()) {
				result.add(i + 1);
			}
		}
		return result;
	}

	// 로또 결과와 사용자의 입력값에 따라 Label의 텍스트를 변경시켜주는 메소드 입니다.
	// 텍스트를 변경시킬 Label, 사용자의 입력 값 리스트를 <Integer>의 형태로, 로또 결과 리스트를 <String>의 형태로
	// 넣어주시면 되며
	// 사용자의 입력 값 리스트는 원래 List<JCheckBox>의 형태일 것이기에
	// returnCheckBoxListToIntegerList메소드를 이용하여 Integer형 리스트를 반환 받아 사용하시면 됩니다.
	public void setLabelTextToResult(JLabel lbl, List<Integer> userList, List<String> resultStrList) {
		List<Integer> resultList = new ArrayList<>();

		for (int i = 0; i < resultStrList.size() - 1; i++) { // 보너스 당첨 번호 제외
			resultList.add(Integer.valueOf(resultStrList.get(i)));
		}

		Integer bonusNum = Integer.valueOf(resultStrList.get(resultStrList.size() - 1));
		boolean containBonusNum = userList.contains(bonusNum);

		int count = 0;

		for (Integer num : userList) {
			if (resultList.contains(num)) {
				count++;
			}
		}

		if (count == 6) {
			lbl.setText("1등 당첨");
		} else if (count == 5 && containBonusNum) {
			lbl.setText("2등 당첨");
		} else if (count == 5) {
			lbl.setText("3등 당첨");
		} else if (count == 4) {
			lbl.setText("4등 당첨");
		} else if (count == 3) {
			lbl.setText("5등 당첨");
		} else {
			lbl.setText("망했습니다.");
		}
		lbl.setFont(fontHolder.getUseFont(Font.BOLD, 20));
	}

	// 사용자가 모든 번호를 선택하였는지를 체크하는 메소드 입니다.
	// 사용자가 체크한 모든 정보가 들어있는 List<List<JCheckBox>>를 파라미터값으로 넣어주세요
	// 만약 모두 6개씩 정상적으로 체크했다면 true를 반환하며,
	// 나머지 상황에서는 false를 반환합니다.
	public boolean checkAllSelected(List<List<JCheckBox>> result) {
		for (int i = 0; i < result.size(); i++) {
			int count = 0;

			for (int j = 0; j < result.get(i).size(); j++) {
				if (result.get(i).get(j).isSelected()) {
					count++;
				}
			}

			if (count != 6) {
				return false;
			}
		}
		return true;
	}

	// 해당 메소드를 사용하였을 때
	// 2등에 해당하면 true를 반환
	// 그 외에는 false를 반환하는 메소드입니다.
	// 2등의 경우 보너스 점수를 활용해야 하는 특이사항 발생으로 인하여 작성하였습니다.
	public boolean check2ndPlace(List<String> winnerList, List<Integer> intList) {
		List<Integer> changeList = new ArrayList<>();

		for (int i = 0; i < winnerList.size() - 1; i++) {
			changeList.add(Integer.valueOf(winnerList.get(i)));
		}

		int count = 0;

		for (int i = 0; i < intList.size(); i++) {
			if (changeList.contains(intList.get(i))) {
				count++;
			}
		}

		if (count == 5) {
			return true;
		}
		return false;
	}

	// 실시간으로 감시하는 타이머를 생성하는 메소드 입니다.
	// 해당 메소드 파라미터 값으로 던져준 체크박스 리스트의 체크된 개수가 6개 이상일시
	// 더 이상 선택할 수 없도록 체크박스를 비활성화 시킵니다.
	public Timer makeTimer(List<JCheckBox> checkBoxList, JLabel oxLabel) {
		Timer timer = new Timer(1, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int count = 0;

				for (JCheckBox box : checkBoxList) {
					if (box.isSelected()) {
						count++;
					}
				}

				if (count == 6) {
					oxLabel.setText("O");

					for (JCheckBox box : checkBoxList) {
						if (box.isSelected()) {
							box.setEnabled(true);
							
						} else {
							box.setEnabled(false);
						}
					}

				} else {
					oxLabel.setText("X");

					for (JCheckBox box : checkBoxList) {
						box.setEnabled(true);
					}
				}
			}
		});
		return timer;
	}

	// 사용자가 제출한 번호 내용을 토대로 saveCheckBox를 구성 및 저장하는 메소드입니다.
	// 이전 기록들을 전부 저장하기 위한 saveCheckBox 리스트 구성
	public void saveCheckBoxNum(List<List<List<JCheckBox>>> saveCheckBox, List<List<JCheckBox>> resultShow) {

		for (int i = 0; i < saveCheckBox.size() + 1; i++) {
			if (i == saveCheckBox.size()) {
				
				List<List<JCheckBox>> newBoxListList = new ArrayList<>();
				for (int j = 0; j < resultShow.size(); j++) {

					List<JCheckBox> newBoxList = new ArrayList<>();
					for (int k = 0; k < 45; k++) {
						JCheckBox checkBox = new JCheckBox();

						if (resultShow.get(j).get(k).isSelected()) {
							checkBox.setSelected(true);
						}
						newBoxList.add(checkBox);
					}
					newBoxListList.add(newBoxList);
				}
				saveCheckBox.add(newBoxListList);
				break;
			}
		}
	}

	// 이전에 제출한 내용대로 체크란을 변경시켜주는 버튼의 기능 활성화 메소드입니다.
	public void beforeBtnFunction(JButton beforeBtn, List<List<List<JCheckBox>>> saveCheckBox,
			List<List<JCheckBox>> resultShow, List<List<Integer>> findBtnList, List<JLabel> labelCollection,
			JComboBox<String> beforeLottoNum, List<Timer> timerCollection) {

		beforeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Timer timer : timerCollection) {
					timer.stop();
				}

				for (int i = 0; i < resultShow.size(); i++) {

					findBtnList.get(i).add(1);

					for (int j = 0; j < resultShow.get(i).size(); j++) {
						resultShow.get(i).get(j).setSelected(false);
						resultShow.get(i).get(j).setEnabled(false);
					}
					labelCollection.get(i).setText("X");
				}

				String choose = beforeLottoNum.getItemAt(beforeLottoNum.getSelectedIndex());
				String firstChar = choose.substring(0, 1);
				int split = Integer.parseInt(firstChar);

				if (saveCheckBox.size() != 0) {

					for (int i = 0; i < saveCheckBox.get(split - 1).size(); i++) {

						for (int j = 0; j < saveCheckBox.get(split - 1).get(i).size(); j++) {

							try {
								if (saveCheckBox.get(split - 1).get(i).get(j).isSelected()) {
									resultShow.get(i).get(j).setSelected(true);
									findBtnList.get(i).add(j + 1);
									labelCollection.get(i).setText("O");
								}
							} catch (Exception e1) {
							}
						}
					}
				}
			}
		});
	}

	// 현재의 체크 내용을 토대로 모든 체크 박스들을 체크하는 메소드입니다.
	// 타이머는 "모든 타이머를 들고있는 리스트<타이머>, 현제 체크박스를 감시하는 타이머" 2가지 종류를 넣어주어야 합니다.
	public void unityCheckBox(List<JCheckBox> checkBoxList, List<List<JCheckBox>> checkBoxListCollection,
			List<List<Integer>> findBtnList, JButton btn, List<JLabel> lblList, List<Timer> timerCollection,
			Timer timer) {

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Timer timer : timerCollection) {
					timer.stop();
				}
				timer.start();
				// 모든 체크박스 비활성화
				for (int i = 0; i < checkBoxListCollection.size(); i++) {
					for (int j = 0; j < checkBoxListCollection.get(i).size(); j++) {
						checkBoxListCollection.get(i).get(j).setEnabled(false);
					}
				}

				// 버튼이 위치한 체크박스는 체크 위치에 따라 활성 및 비활성화
				int count = 0;
				List<Integer> indexList = new ArrayList<>();
				for (int i = 0; i < checkBoxList.size(); i++) {
					if (checkBoxList.get(i).isSelected()) {
						checkBoxList.get(i).setEnabled(true);
						indexList.add(i);
						count++;
					} else {
						checkBoxList.get(i).setEnabled(false);
					}
				}

				if (count == 6) {

					for (List<JCheckBox> list : checkBoxListCollection) {
						for (int i = 0; i < list.size(); i++) {

							if (indexList.contains(i)) {
								list.get(i).setSelected(true);
							} else {
								list.get(i).setSelected(false);
							}
						}
					}

					for (int i = 0; i < findBtnList.size(); i++) {
						while (findBtnList.get(i).size() != 0) {
							findBtnList.get(i).remove(0);
						}

						findBtnList.get(i).add(1);

						for (int j = 0; j < indexList.size(); j++) {
							findBtnList.get(i).add(indexList.get(j) + 1);
						}
						
						lblList.get(i).setText("O");
					}

				} else {
					JOptionPane.showMessageDialog(FunctionList.this, "번호 6개를 모두 선택해주세요.");
				}
			}
		});
	}

	// 전체 취소 버튼 기능 할당 메소드
	public void cancelAll(JButton cancelBtn, List<List<JCheckBox>> checkBoxList, List<Timer> timerCollection,
			List<JLabel> labelCollection) {

		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Timer timer : timerCollection) {
					timer.stop();
				}

				for (int i = 0; i < checkBoxList.size(); i++) {
					for (int j = 0; j < checkBoxList.get(i).size(); j++) {
						checkBoxList.get(i).get(j).setEnabled(false);
						checkBoxList.get(i).get(j).setSelected(false);
					}
					labelCollection.get(i).setText("X");
				}
			}
		});
	}

	// 제출하기 버튼을 눌렀을 때 돈을 차감시키는 메소드 입니다.
	public int payMoney(int money, List<List<JCheckBox>> resultShow) {
		money -= (resultShow.size() * 1000);

		return money;
	}

	// 패널안에 있는 레이블의 텍스트가 "망했습니다." 인 패널들을 삭제하고 당첨 된 패널만 출력하는 메소드입니다.
	public void printWinNumPnlBtnFunction(JButton btn, JPanel resultMainPnl, List<JPanel> pnlList,
			List<JLabel> labelCollection) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < labelCollection.size(); i++) {
					if (labelCollection.get(i).getText().equals("망했습니다.")) {
						resultMainPnl.remove(pnlList.get(i));
					}
				}
				resultMainPnl.revalidate();
				resultMainPnl.repaint();
			}
		});
	}

	// 체크박스를 선택하는 기능 중 중복된 내용을 메소드화
	private void changecheckBoxEnableToTrue(List<JCheckBox> checkBoxList) {
		for (int i = 0; i < checkBoxList.size(); i++) {
			checkBoxList.get(i).setEnabled(true);
		}
	}

	// 체크박스를 선택하는 기능 중 중복된 내용을 메소드화
	// 추가 건의사항을 구현하기 위하여 반환 타입을 List<Integer>로 변경
	private List<Integer> selectCheckBox(List<JCheckBox> checkBoxList, int count) {

		List<Integer> result = new ArrayList<>();

		while (count < 6) {
			Random random = new Random();
			int index = random.nextInt(45);

			if (!checkBoxList.get(index).isSelected()) { // 체크박스가 선택되어있지않으면
				checkBoxList.get(index).setSelected(true); // 체크박스를 선택함
				count++; // 체크박스를 선택할때마다 count 증가
				result.add(index + 1);
			}
		}
		Collections.sort(result); // 리스트 정렬
		return result;
	}

	// 버튼(자동, 수동, 반자동) 기능을 구현하는 메소드 내용 중 중복된 내용을 메소드 화
	private void listComposition(List<Integer> findBtn, List<Integer> list, int btnDivideNum) {
		findBtn.clear(); // 이전 기록과 상관없이 다른 버튼을 누르면 각 버튼의 역할을 알려주는 리스트를 비움 (다시 구성하기위해)
		findBtn.add(btnDivideNum);

		for (Integer num : list) {
			findBtn.add(num);
		}
	}
}