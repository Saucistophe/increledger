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
    translation: string;
    count: number;
    cap: number;
}

export type Population = {
    name: string;
    translation: string;
    count: number;
    cap: number;
    occupations: Occupation[];
    freePopulation: number;
};


export type Tech = {
    name: string;
    translation: string;
    count: number;
    cap: number;
    cost: Map<string, number>;
};

export type DialogChoice = {
    name: string;
    translation: string;
}

export type Dialog = {
    name: string;
    translation: string;
    choices: DialogChoice[];
};

export type GameDescription = {
    populations: Population[];
    resources: Resource[];
    techs: Tech[];
    dialogs: Dialog[];
}
