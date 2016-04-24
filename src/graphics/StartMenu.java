package graphics;

import AI.AI.AiDifficulty;
import graphics.GLPanel.Orientation;
import graphics.GLView.ClickListener;
import logic.WumpusWorld;
import logic.WumpusWorld.Difficulty;
import math.Vector2f;
import math.Vector3f;

public class StartMenu {
	private GLPanel panel;
	private GLView startBtn;
	
	private boolean started = false;
	private WumpusWorld.Difficulty difficulty = Difficulty.easy;
	private AiDifficulty aiDifficulty = AiDifficulty.none;
	private boolean multiplayer = false;
	
	final Texture easyUnselected = new Texture("res/sprites/easy.PNG");
	final Texture mediumUnselected = new Texture("res/sprites/medium.PNG");
	final Texture hardUnselected = new Texture("res/sprites/hard.PNG");
	final Texture easySelected = new Texture("res/sprites/easySelected.PNG");
	final Texture mediumSelected = new Texture("res/sprites/mediumSelected.png");
	final Texture hardSelected = new Texture("res/sprites/hardSelected.PNG");
	final Texture noneUnselected = new Texture("res/sprites/none.PNG");
	final Texture noneSelected = new Texture("res/sprites/noneSelected.PNG");
	
	
	public void CheckClicked(Vector2f vec){
		panel.CheckClicked(vec);
		if(startBtn.CheckClicked(vec)) {
			startBtn.OnClick();
		}
	}
	
	public StartMenu(){
		init();
	}
	
	public void Draw(){
		panel.Draw();
		startBtn.Draw();
	}
	
	public void Deallocate(){
		panel.Deallocate();
		startBtn.Deallocate();
	}
	
	public boolean isStarted(){
		return started;
	}
	
	public Difficulty getDifficulty(){
		return difficulty;
	}
	
	public AiDifficulty getAiDifficulty(){
		return aiDifficulty;
	}
	
	public boolean isMultiplayer(){
		return multiplayer;
	}
	
	private void init(){
		Window.clear();
		
		panel = new GLPanel(2.0f, 2.0f, Orientation.vertical, .05f, .45f, .05f);
		panel.SetTexture("res/sprites/difficultyTexture.jpg");
		panel.Translate(new Vector3f(-1.0f, -1.0f, 0.0f));
		
		initStartBtn();
		
		initDifficultyPanel();
		
		initAiPanel();
		
		GLPanel multiplayerPanel = new GLPanel(2f, .4f, GLPanel.Orientation.horizontal, .0f, .0f, .025f);
		multiplayerPanel.SetTexture("res/sprites/difficultyTexture.jpg");
		//difficultyPanel.Translate(new Vector3f(-.9f, -.2f, 0.0f));
		panel.AddView(multiplayerPanel);
		
		final GLView multiplayerLabel = new GLView(.60f, .20f);
		multiplayerLabel.SetTexture(new Texture("res/sprites/multiplayer.PNG"));
		multiplayerPanel.AddView(multiplayerLabel);
		
		
		//difficultyPanel.InitBuffers();
		panel.InitBuffers();
		
		Window.render();
	}
	
	private void initStartBtn(){
		startBtn = new GLView(.4f, .3f);
		startBtn.SetTexture("res/sprites/start.PNG");
		startBtn.Translate(new Vector3f(-.15f, 0.65f, 0.0f));
		startBtn.InitBuffers();
		startBtn.SetListener(new ClickListener(){

			@Override
			public void OnClick() {
				//running = true;
				started = true;
				System.out.println("start");
			}
			
		});
	}
	
	private void initDifficultyPanel(){
		GLPanel difficultyPanel = new GLPanel(2f, .4f, GLPanel.Orientation.horizontal, .0f, .0f, .025f);
		difficultyPanel.SetTexture("res/sprites/difficultyTexture.jpg");
		//difficultyPanel.Translate(new Vector3f(-.9f, -.2f, 0.0f));
		panel.AddView(difficultyPanel);
		
		final GLView boardDiffLabel = new GLView(.70f, .20f);
		boardDiffLabel.SetTexture(new Texture("res/sprites/boardDiff.PNG"));
		difficultyPanel.AddView(boardDiffLabel);
		
		final GLView easyBtn = new GLView(.34f, .20f);
		final GLView mediumBtn = new GLView(.44f, .20f);
		final GLView hardBtn = new GLView(.34f, .20f);
		
		easyBtn.SetTexture(easySelected);
		mediumBtn.SetTexture(mediumUnselected);
		hardBtn.SetTexture(hardUnselected);
		
		
		difficultyPanel.AddView(easyBtn);
		easyBtn.SetListener(new ClickListener(){

			@Override
			public void OnClick() {
				difficulty = Difficulty.easy;
				easyBtn.SetTexture(easySelected);
				mediumBtn.SetTexture(mediumUnselected);
				hardBtn.SetTexture(hardUnselected);
				
				//System.out.println("easy");
			}
			
		});
		
		difficultyPanel.AddView(mediumBtn);
		mediumBtn.SetListener(new ClickListener(){

			@Override
			public void OnClick() {
				difficulty = Difficulty.medium;
				easyBtn.SetTexture(easyUnselected);
				mediumBtn.SetTexture(mediumSelected);
				hardBtn.SetTexture(hardUnselected);
				
				//System.out.println("medium");
			}
			
		});
		
		difficultyPanel.AddView(hardBtn);
		hardBtn.SetListener(new ClickListener(){

			@Override
			public void OnClick() {
				difficulty = Difficulty.hard;
				easyBtn.SetTexture(easyUnselected);
				mediumBtn.SetTexture(mediumUnselected);
				hardBtn.SetTexture(hardSelected);
				
				
				//System.out.println("hard");
			}
			
		});
	}
	
	private void initAiPanel(){
		GLPanel aiPanel = new GLPanel(2f, .4f, GLPanel.Orientation.horizontal, .0f, .0f, .025f);
		aiPanel.SetTexture("res/sprites/difficultyTexture.jpg");
		//difficultyPanel.Translate(new Vector3f(-.9f, -.2f, 0.0f));
		panel.AddView(aiPanel);
		
		final GLView aiLabel = new GLView(.30f, .20f);
		aiLabel.SetTexture(new Texture("res/sprites/ai.PNG"));
		aiPanel.AddView(aiLabel);
		
		final GLView aiNoneBtn = new GLView(.34f, .20f);
		final GLView aiEasyBtn = new GLView(.34f, .20f);
		final GLView aiMediumBtn = new GLView(.44f, .20f);
		final GLView aiHardBtn = new GLView(.34f, .20f);
		
		aiNoneBtn.SetTexture(noneSelected);
		aiEasyBtn.SetTexture(easyUnselected);
		aiMediumBtn.SetTexture(mediumUnselected);
		aiHardBtn.SetTexture(hardUnselected);
		
		aiPanel.AddView(aiNoneBtn);
		aiNoneBtn.SetListener(new ClickListener(){

			@Override
			public void OnClick() {
				aiDifficulty = AiDifficulty.none;
				aiNoneBtn.SetTexture(noneSelected);
				aiEasyBtn.SetTexture(easyUnselected);
				aiMediumBtn.SetTexture(mediumUnselected);
				aiHardBtn.SetTexture(hardUnselected);
			}
			
		});
		
		
		aiPanel.AddView(aiEasyBtn);
		aiEasyBtn.SetListener(new ClickListener(){

			@Override
			public void OnClick() {
				aiDifficulty = AiDifficulty.easy;
				aiNoneBtn.SetTexture(noneUnselected);
				aiEasyBtn.SetTexture(easySelected);
				aiMediumBtn.SetTexture(mediumUnselected);
				aiHardBtn.SetTexture(hardUnselected);
				
				//System.out.println("easy");
			}
			
		});
		
		aiPanel.AddView(aiMediumBtn);
		aiMediumBtn.SetListener(new ClickListener(){

			@Override
			public void OnClick() {
				aiDifficulty = AiDifficulty.medium;
				aiNoneBtn.SetTexture(noneUnselected);
				aiEasyBtn.SetTexture(easyUnselected);
				aiMediumBtn.SetTexture(mediumSelected);
				aiHardBtn.SetTexture(hardUnselected);
				
				//System.out.println("medium");
			}
			
		});
		
		aiPanel.AddView(aiHardBtn);
		aiHardBtn.SetListener(new ClickListener(){

			@Override
			public void OnClick() {
				aiDifficulty = AiDifficulty.hard;
				aiNoneBtn.SetTexture(noneUnselected);
				aiEasyBtn.SetTexture(easyUnselected);
				aiMediumBtn.SetTexture(mediumUnselected);
				aiHardBtn.SetTexture(hardSelected);
				
				
				//System.out.println("hard");
			}
			
		});
	}
}