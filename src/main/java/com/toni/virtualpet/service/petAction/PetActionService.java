package com.toni.virtualpet.service.petAction;

public class PetActionService {
}

/*@Service
public class PetActionService {

    @Autowired
    private PetActionRepository petActionRepository;

    public void applyAction(Pet pet, User user, ActionType actionType) {
        switch (actionType) {
            case FEED -> {
                pet.setHunger(Math.min(100, pet.getHunger() + 20));
                pet.setHappiness(Math.min(100, pet.getHappiness() + 10));
            }
            case PLAY -> {
                pet.setHappiness(Math.min(100, pet.getHappiness() + 15));
                pet.setEnergy(Math.max(0, pet.getEnergy() - 10));
                pet.setHunger(Math.max(0, pet.getHunger() - 5));
            }
            case REST -> {
                pet.setEnergy(Math.min(100, pet.getEnergy() + 25));
                pet.setHappiness(Math.min(100, pet.getHappiness() + 5));
                pet.setHunger(Math.max(0, pet.getHunger() - 5));
            }
            case IGNORE -> {
                pet.setHappiness(Math.max(0, pet.getHappiness() - 10));
                pet.setEnergy(Math.max(0, pet.getEnergy() - 5));
                pet.setHunger(Math.max(0, pet.getHunger() - 5));
            }
        }

        pet.setLastAction(LocalDateTime.now());
        pet.addExperience(actionType.getExperienceReward());

        PetAction action = new PetAction();
        action.setActionType(actionType);
        action.setPet(pet);
        action.setUser(user);
        action.setExperienceGained(actionType.getExperienceReward());

        petActionRepository.save(action);
    }
}*/