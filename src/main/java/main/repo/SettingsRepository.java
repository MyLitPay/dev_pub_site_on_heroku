package main.repo;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<GlobalSettings, Integer> {
}
