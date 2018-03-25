namespace :dev do
  desc "Popula dados para teste"
  task setup: :environment do

    puts "Cadastrando survivor"
    100.times do |i|
      Survivor.create!(
          name: Faker::Name.name,
          age: Faker::Number.between(21, 60),
          gender: [:FEMALE, :MALE].sample,
          infected: [true, false].sample,
          location: Location.create!(
              latitude: Faker::Address.latitude,
              longitude: Faker::Address.longitude
          )
      )
    end
    puts "Survivor cadastrado com sucesso!"

  end

end
