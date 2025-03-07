export type Resource = {
    name: string;
    translation: string;
    amount: number;
    cap: number;
    production: number;
    emoji: string;
}


export type Occupation = {
    name: string;
    count: number;
    cap: number;
}

export type Population = {
    name: string;
    count: number;
    cap: number;
    occupations: Occupation[];
    freePopulation: number;
};


export type Tech = {
    name: string;
    count: number;
    cap: number;
    cost: Map<string, number>;
};

export type Dialog = {
    name: string;
    choices: string[];
};

export type GameDescription = {
    populations: Population[];
    resources: Resource[];
    techs: Tech[];
    dialogs: Dialog[];
}
